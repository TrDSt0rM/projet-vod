# Étape 1 : Build de l'application avec Gradle
# On utilise l'image officielle qui contient déjà Gradle et le JDK 21
FROM gradle:8-jdk21-alpine AS build
WORKDIR /workspace/app

# On copie tout le contenu du projet dans le conteneur
COPY . .

# On lance la compilation. On utilise 'gradle' au lieu de './gradlew'
# pour éviter les soucis de wrapper manquant ou de permissions.
RUN gradle clean bootJar --no-daemon

# Étape 2 : Image d'exécution légère
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# On récupère uniquement le fichier .jar généré à l'étape précédente
COPY --from=build /workspace/app/build/libs/*.jar app.jar

# Port par défaut pour Spring Boot (sera mappé dans le docker-compose)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]