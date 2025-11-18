# Multi-stage build for optimized Docker image

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create runtime image
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create uploads directory for file storage
RUN mkdir -p /app/uploads/attachments && \
    chmod -R 777 /app/uploads

# Copy the built JAR from build stage
COPY --from=build /app/target/emailbulksender-*.jar app.jar

# Expose port (Render will use the PORT environment variable)
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

