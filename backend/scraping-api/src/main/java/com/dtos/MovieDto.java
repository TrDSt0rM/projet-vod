package com.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class MovieDto {
	
	@NotNull
	private String title;

    private Integer realisationYear;

    private Integer minimalYear;

    private boolean isRentable;

    private List<GenreDto> genres;

    private List<ArtistDto> artists;

    private String posterUrl;

    @NotBlank (message = "Veuillez renseigner le lien du site scrapé")
    private String sourceUrl;
}
