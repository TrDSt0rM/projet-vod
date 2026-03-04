package com.repositories;

import com.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByType(String type);

    List<Artist> findByNomContainingIgnoreCase(String nom);
}