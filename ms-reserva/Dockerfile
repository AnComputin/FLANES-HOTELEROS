FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Toma el JAR que ya compilaste en tu máquina
COPY target/ms-reserva-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]