# Etapa de build (compila el JAR con Maven)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de runtime (ejecuta el JAR con Java 21)
FROM eclipse-temurin:21-jdk
VOLUME /tmp
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8585 
ENTRYPOINT ["java", "-jar", "/app.jar"]