package com.dtos;

import com.enums.ScrapingTaskStatus;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Class de l'objet renvoyé au client.
 */
@Data
public class ScrapingTaskDto {

    @Id
    private String id;

    @NotBlank (message = "Le status de la tâche de scraping est nécessaire")
    private ScrapingTaskStatus status;

    private MovieDto result;

}
