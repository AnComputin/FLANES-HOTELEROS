# 1. Usamos una imagen base que ya tiene Java 21 instalado (como un sistema operativo mini)
FROM eclipse-temurin:21-jdk-alpine

# 2. Le decimos a Docker: "El código va a correr en este puerto, déjalo abierto"
EXPOSE 8082

# 3. Copiamos el archivo .jar (tu código compilado) desde tu compu hacia dentro de la imagen Docker
COPY target/ms-habitacion-0.0.1-SNAPSHOT.jar app.jar

# 4. El comando que se ejecutará cuando alguien encienda el contenedor
ENTRYPOINT ["java", "-jar", "/app.jar"]