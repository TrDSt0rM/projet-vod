FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace/app
COPY . .
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]