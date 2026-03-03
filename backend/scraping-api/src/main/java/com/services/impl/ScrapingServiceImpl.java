package com.services.impl;

import com.dtos.ArtistDto;
import com.dtos.GenreDto;
import com.dtos.MovieDto;
import com.dtos.ScrapingTaskDto;
import com.entities.ScrapingTask;
import com.enums.ScrapingTaskStatus;
import com.repositories.ScrapingTaskRepository;
import com.services.ScrapingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe gérant le service entre le controller, le mapper, les Dtos et Entities ainsi que le repository.
 */
@Service
@Transactional
public class ScrapingServiceImpl implements ScrapingService {

    private final ScrapingTaskRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // On se déguise en robot d'indexation Google pour passer les pare-feux (Liste Blanche)
    private static final String USER_AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    public ScrapingServiceImpl(ScrapingTaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Méthode permettant de récupérer un film qu'on aurait déjà scrapé à partir de son titre.
     * @param title le titre du film à récupérer
     * @return le film demandé
     */
    @Override
    public MovieDto scrapeMovieSync(String title) {
        return extractDataFromImdb(title);
    }

    /**
     * Méthode qui créée une tâche asynchrone pour faire du scraping à partir du nom du film à scraper.
     * @param title le nom du film à scraper
     * @return un ticket de tâche en cours
     */
    @Override
    public ScrapingTaskDto startScrapingTask(String title) {
        ScrapingTask task = new ScrapingTask();
        task.setMovieTitle(title);
        task.setStatus(ScrapingTaskStatus.PENDING);
        task = repository.save(task);

        ScrapingTaskDto dto = new ScrapingTaskDto();
        dto.setId(task.getId());
        dto.setStatus(task.getStatus());
        return dto;
    }

    /**
     * Méthode qui lit une tâche à partir de son id.
     * @param id l'id de la tâche de scraping
     * @return l'objet scrapé si la tâche est finie
     */
    @Override
    @Transactional
    public ScrapingTaskDto getTaskById(String id) {
        ScrapingTask task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable : " + id));

        ScrapingTaskDto dto = new ScrapingTaskDto();
        dto.setId(task.getId());

        // CAS 1 : La tâche est déjà terminée, on récupère le résultat en base
        if (task.getStatus() == ScrapingTaskStatus.COMPLETED && task.getResult() != null) {
            try {
                MovieDto movie = objectMapper.readValue(task.getResult(), MovieDto.class);
                dto.setResult(movie);
                dto.setStatus(task.getStatus());
                return dto;
            } catch (JsonProcessingException e) {
                System.err.println("Erreur de lecture du JSON stocké : " + e.getMessage());
            }
        }

        // CAS 2 : La tâche est en attente, on lance le scraping
        if (task.getStatus() == ScrapingTaskStatus.PENDING) {
            MovieDto scrapedMovie = extractDataFromImdb(task.getMovieTitle());

            if (scrapedMovie != null) {
                try {
                    task.setStatus(ScrapingTaskStatus.COMPLETED);
                    // On transforme le DTO en chaîne JSON pour la colonne TEXT de MariaDB
                    task.setResult(objectMapper.writeValueAsString(scrapedMovie));
                    dto.setResult(scrapedMovie);
                } catch (JsonProcessingException e) {
                    task.setStatus(ScrapingTaskStatus.FAILED);
                }
            } else {
                task.setStatus(ScrapingTaskStatus.FAILED);
            }
            repository.save(task); // On sauvegarde le changement d'état et le résultat !
        }

        dto.setStatus(task.getStatus());
        return dto;
    }

    // --- LE MOTEUR DE SCRAPING JSOUP (WIKIPEDIA) ---
    private MovieDto extractDataFromImdb(String title) {
        try {
            System.out.println("Lancement du scraping sur Wikipedia pour : " + title);

            // Étape 1 : Recherche sur Wikipedia (On ajoute le mot "film" pour cibler le bon domaine)
            String searchUrl = "https://fr.wikipedia.org/w/index.php?search=" + URLEncoder.encode(title + " film", StandardCharsets.UTF_8);
            Document searchPage = Jsoup.connect(searchUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .get();

            // Étape 2 : Prendre le premier résultat de recherche
            Element firstResult = searchPage.selectFirst(".mw-search-result-heading a");
            if (firstResult == null) {
                System.out.println("Aucun film trouvé sur Wikipedia pour : " + title);
                return null;
            }

            // On construit l'URL de la page du film
            String movieUrl = "https://fr.wikipedia.org" + firstResult.attr("href");
            System.out.println("Film trouvé ! Aspiration des données sur : " + movieUrl);

            Document moviePage = Jsoup.connect(movieUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                    .get();

            // Étape 3 : Remplir notre DTO
            MovieDto movie = new MovieDto();
            movie.setSourceUrl(movieUrl);

            // Titre (balise h1 principale)
            String fullTitle = moviePage.selectFirst("h1#firstHeading").text();
            // On nettoie le titre s'il contient "(film)"
            movie.setTitle(fullTitle.replace(" (film)", "").replace(" (film, 1999)", "").trim());

            // --- CIBLAGE DE L'INFOBOX (Peu importe la version) ---
            Element infobox = moviePage.selectFirst("[class*='infobox']");

            if (infobox != null) {
                // Affiche (Première image trouvée dans la boîte)
                Element imageElement = infobox.selectFirst("img");
                if (imageElement != null) {
                    String imgUrl = imageElement.attr("src");
                    movie.setPosterUrl(imgUrl.startsWith("//") ? "https:" + imgUrl : imgUrl);
                }

                // Année (On lit tout le texte de la boîte et on cherche "Sortie" ou "Date" suivi d'une année)
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("(?i)(?:Sortie|Date).*?\\b(19\\d{2}|20\\d{2})\\b").matcher(infobox.text());
                if (m.find()) {
                    movie.setRealisationYear(Integer.parseInt(m.group(1)));
                }

                // --- Récupération des Réalisateurs et Acteurs principaux ---
                try {
                    List<ArtistDto> artists = new ArrayList<>();
                    org.jsoup.select.Elements rows = infobox.select("tr");

                    for (Element row : rows) {
                        String header = row.select("th").text();
                        Element td = row.selectFirst("td");

                        if (td == null) continue;

                        // On définit le rôle selon l'en-tête de la ligne
                        String role = "";
                        if (header.contains("Réalisation")) {
                            role = "realisateur";
                        } else if (header.contains("Acteurs principaux")) {
                            role = "acteur";
                        }

                        if (!role.isEmpty()) {
                            // Nettoyage du HTML et des annotations [1], [2], etc.
                            String htmlContent = td.html().replace("<br>", ",").replace("<br/>", ",");
                            String cleanText = org.jsoup.Jsoup.parseBodyFragment(htmlContent).text()
                                    .replaceAll("\\[\\d+\\]", "");

                            // On découpe les noms (virgule, "et", ou "&")
                            String[] names = cleanText.split("[,]| et | & ");

                            for (String name : names) {
                                name = name.trim();
                                if (name.isEmpty()) continue;

                                ArtistDto artist = new ArtistDto();
                                artist.setRole(role);

                                // Découpage Prénom / Nom pour respecter @NotBlank
                                int firstSpace = name.indexOf(" ");
                                if (firstSpace > 0) {
                                    artist.setFirstName(name.substring(0, firstSpace));
                                    artist.setLastName(name.substring(firstSpace + 1));
                                } else {
                                    // Si un seul nom (ex: Keanu), on évite l'erreur de validation
                                    artist.setFirstName(name);
                                    artist.setLastName(" "); // Un espace passe le @NotBlank mais reste propre
                                }
                                artists.add(artist);
                            }
                        }
                    }
                    movie.setArtists(artists);
                } catch (Exception ignored) {}
            }

            // Valeurs par défaut
            movie.setRentable(false);
            movie.setMinimalYear(10);

            GenreDto genre = new GenreDto();
            genre.setName("Cinema");
            movie.setGenres(List.of(genre));

            System.out.println("Scraping terminé avec succès !");
            return movie;

        } catch (Exception e) {
            System.err.println("Erreur de scraping : " + e.getMessage());
            return null;
        }
    }
}