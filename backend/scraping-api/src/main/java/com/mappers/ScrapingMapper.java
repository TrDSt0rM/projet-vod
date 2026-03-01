package com.mappers;

import com.dtos.MovieDto;
import com.entities.ScrapingTask;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités Dog et les DTOs DogDto.
 * Un mapper permet de séparer la couche de persistance de la couche de présentation.
 * 
 * Points clés du pattern Mapper :
 * - Conversion bidirectionnelle entre DTO et Entity
 * - Gestion des null-safety
 * - Pas de logique métier, uniquement de la transformation
 */
@Component
public class DogMapper {

    /**
     * Convertit une entité Dog en DTO DogDto
     * Cette méthode est utilisée pour exposer les données aux clients de l'API
     * 
     * @param scrapingTask l'entité à convertir
     * @return le DTO correspondant ou null si l'entité est null
     */
    public MovieDto toDto(ScrapingTask scrapingTask) {
        if (scrapingTask == null) {
            return null;
        }
        
        MovieDto movieDto = new MovieDto();
        movieDto.setId(scrapingTask.getId());
        movieDto.setName(scrapingTask.getName());
        movieDto.setRace(scrapingTask.getRace());
        return movieDto;
    }

    /**
     * Convertit un DTO DogDto en entité Dog
     * Cette méthode est utilisée pour persister les données reçues des clients
     * Note: La date de naissance n'est pas dans le DTO mais est présente dans l'entité
     * 
     * @param movieDto le DTO à convertir
     * @return l'entité correspondante ou null si le DTO est null
     */
    public ScrapingTask toEntity(MovieDto movieDto) {
        if (movieDto == null) {
            return null;
        }

        ScrapingTask scrapingTask = new ScrapingTask();
        // On ne set l'ID que s'il existe (cas d'une mise à jour)
        if (movieDto.getId() != null) {
            scrapingTask.setId(movieDto.getId());
        }
        scrapingTask.setName(movieDto.getName());
        scrapingTask.setRace(movieDto.getRace());
        return scrapingTask;
    }
} 