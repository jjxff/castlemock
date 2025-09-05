# Multi-stage build for Castle Mock Community Fork
FROM eclipse-temurin:21-jdk-alpine AS build

# Install Node.js and yarn for frontend build
RUN apk add --no-cache nodejs npm yarn

# Set working directory
WORKDIR /app

# Copy source code
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runtime

# Add metadata
LABEL org.opencontainers.image.title="Castle Mock Community Fork"
LABEL org.opencontainers.image.description="Mock RESTful APIs and SOAP web services"
LABEL org.opencontainers.image.version="1.69.0"
LABEL org.opencontainers.image.source="https://github.com/jjxff/castlemock"

# Create non-root user
RUN addgroup -g 1000 castlemock && \
    adduser -D -s /bin/sh -u 1000 -G castlemock castlemock

# Create application directory and data directory
RUN mkdir -p /app /home/castlemock/.castlemock && \
    chown -R castlemock:castlemock /app /home/castlemock

# Copy the built JAR from build stage
COPY --from=build --chown=castlemock:castlemock /app/deploy/deploy-jetty/deploy-jetty-jar/target/castlemock.jar /app/

# Switch to non-root user
USER castlemock

# Set working directory
WORKDIR /app

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/castlemock/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "castlemock.jar"]
CMD ["--server.port=8080"]