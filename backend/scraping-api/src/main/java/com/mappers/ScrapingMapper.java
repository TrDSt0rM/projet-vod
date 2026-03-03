package com.mappers;

import com.dtos.MovieDto;
import com.dtos.ScrapingTaskDto;
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
public class ScrapingMapper {

    /**
     * Convertit une entité ScrapingTask en DTO ScrapingTaskDto
     * Cette méthode est utilisée pour exposer les données aux clients de l'API
     * 
     * @param scrapingTask l'entité à convertir
     * @return le DTO correspondant ou null si l'entité est null
     */
    public ScrapingTaskDto toDto(ScrapingTask entity) {
        if (entity == null) {
            return null;
        }

        ScrapingTaskDto dto = new ScrapingTaskDto();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        // Note : On gérera la transformation du 'result' (String) en MovieDto plus tard !
        return dto;
    }

    /**
     * Convertit un DTO ScrapingTaskDto en entité ScrapingTask
     * Cette méthode est utilisée pour persister les données reçues des clients
     * 
     * @param dto la ScrapingTaskDto à convertir
     * @return l'entité correspondante ou null si le DTO est null
     */
    public ScrapingTask toEntity(ScrapingTaskDto dto) {
        if (dto == null) {
            return null;
        }

        ScrapingTask entity = new ScrapingTask();
        entity.setId(dto.getId());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}