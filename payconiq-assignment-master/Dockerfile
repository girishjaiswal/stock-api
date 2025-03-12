# Use an official Java runtime as a parent image for the build stage
FROM openjdk:17-jdk-slim as build

# Install Maven
RUN apt-get update && apt-get install -y maven && apt-get clean

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file (pom.xml) to the container
COPY pom.xml .

# Download the dependencies (this layer will be cached)
RUN mvn dependency:go-offline

# Copy the rest of your application code
COPY src /app/src

# Build the application (you can specify profiles like "dev" or "prod" here)
RUN mvn clean package -DskipTests

# Start a new image from a smaller base image to minimize size
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build image to the new image
COPY --from=build /app/target/stock-api-*.jar /app/stock-api.jar

# Expose the port that Spring Boot uses by default (8080)
EXPOSE 8080

# Define the command to run your Spring Boot application
CMD ["java", "-jar", "/app/stock-api.jar"]
