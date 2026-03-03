package com.services.impl;

import com.dtos.ArtistDto;
import com.dtos.GenreDto;
import com.dtos.MovieDto;
import com.dtos.ScrapingTaskDto;
import com.entities.ScrapingTask;
import com.enums.ScrapingTaskStatus;
import com.repositories.ScrapingTaskRepository;
import com.services.ScrapingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScrapingServiceImpl implements ScrapingService {

    private final ScrapingTaskRepository repository;

    public ScrapingServiceImpl(ScrapingTaskRepository repository) {
        this.repository = repository;
    }

    // --- 1. TEST DE LA MÉTHODE SYNCHRONE ---
    @Override
    public MovieDto scrapeMovieSync(String title) {
        System.out.println("Recherche synchrone simulée pour : " + title);
        return createDummyMovie(title);
    }

    // --- 2. TEST DE LA CRÉATION DE TÂCHE ---
    @Override
    public ScrapingTaskDto startScrapingTask(String title) {
        System.out.println("Création d'une tâche pour : " + title);

        // On sauvegarde en base de données
        ScrapingTask task = new ScrapingTask();
        task.setMovieTitle(title);
        task.setStatus(ScrapingTaskStatus.PENDING);
        task = repository.save(task);

        // On renvoie le DTO
        ScrapingTaskDto dto = new ScrapingTaskDto();
        dto.setId(task.getId());
        dto.setStatus(task.getStatus());
        return dto;
    }

    // --- 3. TEST DE LA LECTURE DE TÂCHE ---
    @Override
    @Transactional(readOnly = true)
    public ScrapingTaskDto getTaskById(String id) {
        ScrapingTask task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tâche introuvable : " + id));

        ScrapingTaskDto dto = new ScrapingTaskDto();
        dto.setId(task.getId());
        dto.setStatus(task.getStatus());

        // Pour le test, si on lit la tâche, on va faire comme si elle était terminée !
        dto.setStatus(ScrapingTaskStatus.COMPLETED);
        dto.setResult(createDummyMovie(task.getMovieTitle()));

        return dto;
    }

    // --- MÉTHODE UTILITAIRE POUR GÉNÉRER UN FAUX FILM ---
    private MovieDto createDummyMovie(String title) {
        MovieDto movie = new MovieDto();
        movie.setTitle(title + " (Version Test)");
        movie.setRealisationYear(2024);
        movie.setMinimalYear(12);
        movie.setRentable(true);
        movie.setPosterUrl("https://fake-url.com/poster.jpg");
        movie.setSourceUrl("https://imdb.com/fake");

        GenreDto genre = new GenreDto();
        genre.setName("Science-Fiction");
        movie.setGenres(List.of(genre));

        ArtistDto director = new ArtistDto();
        director.setFirstName("Christopher");
        director.setLastName("Nolan");
        director.setRole("Réalisateur");
        movie.setArtists(List.of(director));

        return movie;
    }
}