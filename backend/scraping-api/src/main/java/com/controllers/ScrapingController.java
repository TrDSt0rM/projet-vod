package com.controllers;

import com.dtos.MovieDto;
import com.dtos.ScrapingTaskDto;
import com.services.ScrapingService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scraping")
public class ScrapingController {

    private final ScrapingService scrapingService;

    public ScrapingController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    // --- 1. ENDPOINT SYNCHRONE ---

    /**
     * GET /api/scraping/movie?title=Avatar
     * Méthode pour obtenir le film à partir de son titre
     * @param title le titre du film
     * @return le film demandé
     */
    @GetMapping("/movie")
    public ResponseEntity<MovieDto> getMovieSync(@RequestParam String title) {
        MovieDto movie = scrapingService.scrapeMovieSync(title);
        if (movie == null) {
            return ResponseEntity.notFound().build(); // Renvoie 404 si non trouvé
        }
        return ResponseEntity.ok(movie); // Renvoie 200 avec le film
    }

    // --- 2. ENDPOINTS ASYNCHRONES ---

    /**
     * POST /api/scraping/tasks
     * Créer une tâche à partir du nom du film récupéré.
     * @param request le nom du film récupéré depuis la méthode POST
     * @return la tâche lancée
     */
    @PostMapping("/tasks")
    public ResponseEntity<ScrapingTaskDto> startTask(@RequestBody TaskRequest request) {
        ScrapingTaskDto task = scrapingService.startScrapingTask(request.getTitle());
        return ResponseEntity.accepted().body(task); // Renvoie 202 (Accepted)
    }

    // GET /api/scraping/tasks/{id}

    /**
     * GET /api/scraping/tasks/{id}
     * Méthode pour récupérer une tâche par son id.
     * @param id l'id de la tâche à récupérer
     * @return renvoie la tâche demandée
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<ScrapingTaskDto> getTaskStatus(@PathVariable String id) {
        ScrapingTaskDto task = scrapingService.getTaskById(id);
        return ResponseEntity.ok(task); // Renvoie 200
    }

    /**
     * Petite classe interne pour lire le JSON du POST : {"title": "Titanic"}
     */
    @Data
    public static class TaskRequest {
        private String title;
    }
}
