# Use the official Gradle image with JDK 17 for the build stage
FROM gradle:7.2-jdk17 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN ./gradlew build --no-daemon

# Use OpenJDK 17 for the runtime stage
FROM openjdk:17-jdk-slim
COPY --from=builder /app/build/libs/demo-0.0.1-SNAPSHOT.jar /demo.jar
ENTRYPOINT ["java","-jar","/demo.jar"]
EXPOSE 8080
