Gestion des artistes :


Contexte :
Ce module fait partie du projet Systèmes d'Information 2025/26 de l'Université de Bretagne Occidentale.
Il expose une API REST permettant de gérer les artistes (acteurs et réalisateurs) d'une plateforme VOD. Les données sont persistées en base SQL et exposées via Spring Boot.

Architecture :
Le projet suit une architecture en couches classique :

Controller → Service → Repository → Database
                ↓
             Mapper
                ↓
              DTO
          
Structure du projet :
com/

├── entities/

│   └── Artist.java
├── dtos/

│   └── ArtistDto.java
├── mappers/

│   └── ArtistMapper.java

├── repositories/

│   └── ArtistRepository.java

├── services/

│   └── ArtistService.java

└── controllers/

    └── ArtistController.java


Technologies :
Java 17+ --- Langage principal
Spring Boot --- Framework applicatif
Jakarta Validation --- Validation des données
MariaDB (via Docker) --- Base de données

API REST
Base URL :
/api/artist
Endpoints
🔹 GET /api/artistes
Retourne la liste complète des artistes.

🔹 GET /api/artistes/actors
Retourne uniquement les artistes de type ACTEUR.

🔹 GET /api/artistes/directors
Retourne uniquement les artistes de type REALISATEUR.

🔹 GET /api/artistes/{id}
Retourne un artiste spécifique par son identifiant.

🔹 POST /api/artistes (Admin)
Crée un nouvel artiste.
Body JSON :
json{
  "nom": "Nolan",
  "prenom": "Christopher",
  "age": 53,
  "type": "REALISATEUR"
}
