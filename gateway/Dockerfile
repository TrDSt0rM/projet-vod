# Étape 1 : Build de l'application
FROM gradle:8-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# On compile sans lancer les tests pour gagner du temps
RUN gradle build -x test

# Étape 2 : Image d'exécution
FROM eclipse-temurin:21-jre
EXPOSE 9000
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]