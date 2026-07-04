# Multi-stage build: builds the fat jar with Gradle, ships only the JRE + jar.
# Build:  docker build -t mvp-server .
# Run:    docker run -p 8080:8080 -e TUS_HEROKU_URL=... -e ADMIN_PASSWORD=... mvp-server

FROM eclipse-temurin:22-jdk AS build
WORKDIR /app
COPY . .
# sed strips Windows line endings, which would break the shebang on Linux
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew :server:bootJar --no-daemon

FROM eclipse-temurin:22-jre
WORKDIR /app
COPY --from=build /app/server/build/libs/server.jar server.jar
# Keep the JVM inside the small memory limits of free hosting tiers
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75"
EXPOSE 8080
CMD ["java", "-jar", "server.jar"]
