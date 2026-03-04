# Movie Scraping Microservice

Ce microservice Java/Spring Boot permet d'extraire automatiquement des données cinématographiques depuis **Wikipédia** pour alimenter une base de données VOD.

## Résumé du Projet
Le service automatise la récupération d'informations essentielles sur les films : titre, année, affiche, réalisateurs, acteurs et genres. Il propose deux modes de fonctionnement :
1.  **Synchrone** : Récupération immédiate des données via `/api/scraping/movie`.
2.  **Asynchrone** : Création d'une tâche avec un identifiant unique (UUID) stockée dans une base de données **MariaDB**.

---

## Stack Technique
* **Langage** : Java 21.
* **Framework** : Spring Boot 3 (Web, Data JPA, Validation).
* **Scraping** : **Jsoup** pour le parsing HTML.
* **Base de données** : MariaDB (via Docker).
* **Sérialisation** : **Jackson (`ObjectMapper`)** pour le stockage du résultat en format JSON (colonne TEXT).
* **Utilitaires** : Lombok (réduction du code boilerplate) et Regex pour le nettoyage des données.

---

## Fonctionnalités Clés
* **Extraction Dynamique** : Analyse de l'infobox Wikipédia pour extraire l'année et les artistes avec gestion des rôles (réalisateur/acteur).
* **Contournement de Blocage** : Simulation d'un User-Agent `Googlebot` pour éviter les erreurs HTTP 403/410.
* **Persistance JSON** : Le résultat du scraping est sauvegardé dans la table `ScrapingTask` pour éviter les requêtes redondantes.
* **Validation des Données** : Utilisation de `@NotBlank` pour assurer l'intégrité des données d'artistes.

---

## Structure du Projet
```text
src/main/java/com/
├── App.java                   # Classe principale (@SpringBootApplication)
├── controllers/
│   └── ScrapingController.java # Endpoints REST (GET/POST)
├── services/
│   ├── ScrapingService.java    # Interface
│   └── impl/
│       └── ScrapingServiceImpl.java # Moteur Jsoup et logique métier
├── entities/
│   └── ScrapingTask.java      # Entité MariaDB avec colonne TEXT result
├── dtos/
│   ├── MovieDto.java          # Modèle de données du film
│   ├── ArtistDto.java         # DTO Artiste (Nom, Prénom, Rôle)
│   └── ScrapingTaskDto.java   # DTO de suivi de tâche
├── repositories/
│   └── ScrapingTaskRepository.java # Interface repository JPA
└── enums/
    └── ScrapingTaskStatus.java # PENDING, COMPLETED, FAILED
```

---

_Made by Alex Plociennik._

---