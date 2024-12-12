# Utiliser une image de base avec Java 17
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/ProfileHama-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
