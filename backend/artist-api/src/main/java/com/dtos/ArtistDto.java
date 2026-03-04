package com.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ArtistDto {

	@NotNull
	private Long id;

	@NotBlank(message = "Le nom est obligatoire")
	private String nom;

	@NotBlank(message = "Le prénom est obligatoire")
	private String prenom;

	@NotNull(message = "L'âge est obligatoire")
	@Positive(message = "L'âge doit être positif")
	private Integer age;

	@NotBlank(message = "Le type est obligatoire (ACTEUR ou REALISATEUR)")
	private String type;
}