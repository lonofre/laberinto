# Do not use this Dockerfile in production
# This should only be used in Dev Env
FROM eclipse-temurin:17.0.11_9-jdk-ubi9-minimal
WORKDIR /app
COPY . /app
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["./mvnw", "spring-boot:run"]
