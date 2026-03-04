# Authentication Service

Ce service gère **l’authentification des utilisateurs** pour l’application VOD.
Il permet aux utilisateurs de créer un compte, de se connecter et d’accéder aux fonctionnalités protégées de l’API.

---

## Description

Le service propose les fonctionnalités suivantes :

* création d’un compte utilisateur
* connexion avec pseudo et mot de passe
* génération d’un **token d’authentification**
* récupération des informations de l’utilisateur connecté

Les utilisateurs sont enregistrés dans une base de données **MariaDB**.

---

## Technologies utilisées

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT** pour l’authentification
* **MariaDB** (base de données)
* **Docker**
* **Swagger** pour tester l’API

---

## Fonctionnement

1. Un utilisateur peut créer un compte avec `/auth/register`.
2. Il peut ensuite se connecter avec `/auth/login`.
3. L’API génère un **token** qui permet d’accéder aux routes protégées.
4. Ce token doit être envoyé dans les requêtes suivantes.

---

## Rôles utilisateurs

Deux rôles existent :

* **USER** : utilisateur normal
* **ADMIN** : administrateur

Les administrateurs peuvent accéder aux fonctionnalités d’administration de l’application.

---

## Endpoints principaux

Créer un compte :

```
POST /auth/register
```

Se connecter :

```
POST /auth/login
```

Voir les informations de l’utilisateur connecté :

```
GET /auth/me
```

---

## Lancer le projet

Lancer la base de données avec Docker :

```
docker compose up -d
```

Puis lancer l’application Spring Boot.

---

## Tester l’API

Swagger est disponible à l’adresse :

```
http://localhost:8080/swagger-ui.html
```

---

*Made by Lynda Merabtene.*
