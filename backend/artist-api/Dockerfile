FROM gradle:8-jdk21-alpine AS build
WORKDIR /workspace/app
COPY . .

RUN gradle clean bootJar --no-daemon
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /workspace/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]