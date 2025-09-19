# Stage 1: build using the Gradle wrapper from the repo root (context = repo root)
FROM gradle:8.6-jdk17 AS builder
WORKDIR /home/gradle/project

# copy everything from repo root (ensure you run docker build from repo root)
COPY . .

# (optional debug) list top-level files to verify the version-catalog and settings are present
RUN ls -la /home/gradle/project && ls -la /home/gradle/project/gradle || true

# Make sure the gradlew wrapper is executable (if present)
RUN if [ -f ./gradlew ]; then chmod +x ./gradlew; fi

# Build the fat jar for the server module. Adjust task name if different.
# We skip tests to speed up build; remove -x test if you want tests.
RUN ./gradlew :server:shadowJar -x test --no-daemon

# Stage 2: runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built fat jar produced by shadowJar.
# The builder produced server/build/libs/*.jar — copy the first matching jar.
COPY --from=builder /home/gradle/project/server/build/libs/*.jar /app/app.jar

# Optional: default JVM options (override with env var JAVA_OPTS)
ENV JAVA_OPTS=""

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=5 \
  CMD curl -f http://localhost:8080/ || exit 1

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
