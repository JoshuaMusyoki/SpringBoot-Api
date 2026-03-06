# Stage 1: Build the JAR using Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /build
# Copy only the pom first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy the source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the JAR from the build stage
COPY --from=build /build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]