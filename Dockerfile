# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy all backend + static frontend (React build)
COPY src ./src

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Start Spring Boot
CMD ["java", "-jar", "target/ecom-backend-0.0.1-SNAPSHOT.jar"]
