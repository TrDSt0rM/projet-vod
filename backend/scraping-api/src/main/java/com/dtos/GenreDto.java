package com.dtos;

import lombok.Data;

/**
 * Classe du genre, utilisé par MovieDto, pour être envoyé au client.
 */
@Data
public class GenreDto {
    private String name;
}
