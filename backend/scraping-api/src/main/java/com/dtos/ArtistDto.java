package com.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Classe des artistes, utilisé par MovieDto, pour être envoyé au client.
 */
@Data
public class ArtistDto {

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    private String role;
}
