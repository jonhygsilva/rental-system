# Multi-stage Dockerfile for Gradle-built Spring Boot Kotlin app

# 1) build stage
FROM gradle:9.3.1-jdk17 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
# do a clean build and assemble the jar
RUN gradle clean bootJar --no-daemon -x test

# 2) runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# copy the jar produced by bootJar (looks in build/libs)
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
# Expose the port the app runs on
EXPOSE 8080
# Use a non-root user for better security
USER 1000
ENTRYPOINT ["sh","-c","java -jar /app/app.jar"]

