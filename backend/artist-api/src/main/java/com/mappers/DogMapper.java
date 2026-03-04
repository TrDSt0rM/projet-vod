package com.mappers;

import com.dtos.ArtistDto;
import com.entities.Artist;
import org.springframework.stereotype.Component;

/**
 * Mapper responsable de la conversion entre les entités Artist et les DTOs ArtistDto.
 *
 * Rôle :
 * - Séparer la couche Entity de la couche DTO
 * - Conversion bidirectionnelle
 * - Null-safety
 */
@Component
public class ArtistMapper {

    /**
     * Convertit une entité Artist en DTO
     */
    public ArtistDto toDto(Artist artist) {
        if (artist == null) {
            return null;
        }

        ArtistDto artistDto = new ArtistDto();
        artistDto.setId(artist.getId());
        artistDto.setNom(artist.getNom());
        artistDto.setPrenom(artist.getPrenom());
        artistDto.setAge(artist.getAge());
        artistDto.setType(artist.getType());

        return artistDto;
    }

    /**
     * Convertit un DTO en entité Artist
     */
    public Artist toEntity(ArtistDto artistDto) {
        if (artistDto == null) {
            return null;
        }

        Artist artist = new Artist();

        // On set l'ID seulement s'il existe (cas update)
        if (artistDto.getId() != null) {
            artist.setId(artistDto.getId());
        }

        artist.setNom(artistDto.getNom());
        artist.setPrenom(artistDto.getPrenom());
        artist.setAge(artistDto.getAge());
        artist.setType(artistDto.getType());

        return artist;
    }
}