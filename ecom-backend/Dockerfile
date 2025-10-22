# Stage 1: Build the application
FROM maven:3.9.3-eclipse-temurin-20 AS build
WORKDIR /app
COPY ecom-backend/pom.xml .
COPY ecom-backend/src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
