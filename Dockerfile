FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw clean install -DskipTests

EXPOSE 8080

CMD ["java", "-Djava.awt.headless=true", "-jar", "target/registry-0.0.1-SNAPSHOT.jar"]
