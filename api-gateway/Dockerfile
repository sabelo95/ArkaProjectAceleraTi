# ===========================
# Etapa 1: Construcción
# ===========================
FROM gradle:8.14-jdk17-alpine AS build

WORKDIR /app

# Copiar configuración de Gradle (para usar caché)
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

RUN ./gradlew dependencies --no-daemon

# Copiar código fuente
COPY src/ src/

# Construir el JAR sin ejecutar los tests
RUN ./gradlew bootJar --no-daemon -x test

# ===========================
# Etapa 2: Imagen final
# ===========================
FROM openjdk:17-jdk-alpine

LABEL maintainer="Equipo de desarrollo"
LABEL description="API Gateway - Arka Project"
LABEL version="0.0.1-SNAPSHOT"

# Crear usuario no-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser

# Puerto donde corre el gateway (ajusta si usas otro)
EXPOSE 8081

# Variables por defecto
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8081/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
