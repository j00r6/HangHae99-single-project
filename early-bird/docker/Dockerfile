# Base image with Gradle
FROM gradle:7.5.1-jdk17 AS build

# Set the working directory
WORKDIR /app

# Copy the necessary files to the container
COPY ../gradlew ./
COPY ../gradle/ ./gradle/
COPY ../build.gradle ./
COPY ../settings.gradle ./
COPY ../src/ ./src/

# Make gradlew executable
RUN chmod +x gradlew

# Build the application, skipping tests
RUN ./gradlew build -x test

# Use a lightweight base image for the final stage
FROM bellsoft/liberica-openjdk-alpine:17

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
