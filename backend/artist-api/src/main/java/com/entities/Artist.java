package com.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nom;

	@Column(nullable = false)
	private String prenom;

	private Integer age;

	@Column(nullable = false)
	private String type;
	// "ACTEUR" ou "REALISATEUR"

	// Films où l'artiste est acteur
	@ManyToMany(mappedBy = "acteurs")
	private Set<Film> filmsActeur = new HashSet<>();

	// Films où l'artiste est réalisateur
	@OneToMany(mappedBy = "realisateur")
	private Set<Film> filmsRealises = new HashSet<>();

	public Artist() {}

	public Artist(String nom, String prenom, Integer age, String type) {
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
		this.type = type;
	}

}