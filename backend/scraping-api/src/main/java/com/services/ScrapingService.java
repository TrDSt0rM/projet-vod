package com.services;

import com.dtos.MovieDto;
import com.dtos.ScrapingTaskDto;

/**
 * Interface définissant les opérations métier disponibles pour la gestion des scraps.
 * Cette interface suit le principe d'Interface Segregation (SOLID).
 */
public interface ScrapingService {
    // Méthode synchrone

    /**
     * Récupère un film de manière synchrone dans la base de scraping.
     * @param title le titre du film à récupérer
     * @return le film récupérer
     */
    MovieDto scrapeMovieSync(String title);

    // Méthodes asynchrones

    /**
     * Lancement de la tâche de scraping d'un film à partir de son titre.
     * @param title le nom du film à scraper
     * @return Renvoie un ticket de ScrapingTaskDto avec pour le moment aucun film scrapé
     */
    ScrapingTaskDto startScrapingTask(String title);

    /**
     * Récupère la tâche scrapée seulement si elle est finie.
     * @param id l'id de la tâche de scraping
     * @return renvoie la tâche de scraping au client
     */
    ScrapingTaskDto getTaskById(String id);
}
