package com.controllers;

import com.entity.Artist;
import com.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistes")
@CrossOrigin
public class ArtistController {

    private final ArtistService artisteService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    // GET tous les artistes
    @GetMapping
    public List<Artist> getAllArtistes() {
        return artistService.getAllArtistes();
    }

    @GetMapping("/acteurs")
    public List<Artist> getActeurs() {
        return artistService.getActeurs();
    }

    @GetMapping("/realisateurs")
    public List<Artist> getRealisateurs() {
        return artistService.getRealisateurs();
    }

    // GET par id
    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable Long id) {
        return artisteService.getArtistById(id)
                .orElseThrow(() -> new RuntimeException("Artiste non trouvé"));
    }

    // POST créer artiste (admin)
    @PostMapping
    public Artist createArtist(@RequestBody Artist artist) {
        return artistService.createArtist(artist);
    }

    // DELETE artiste (admin)
    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Long id) {
        artisteService.deleteArtiste(id);
    }
}