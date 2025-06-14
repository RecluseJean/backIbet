FROM openjdk:17-jdk-slim

# Instala librerías necesarias para Java AWT headless (para generación de Excel con estilos y fuentes)
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fonts-dejavu \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["java", "-Djava.awt.headless=true", "-jar", "target/registry-0.0.1-SNAPSHOT.jar"]