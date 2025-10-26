# ===========================
# Etapa 1: Construcción
# ===========================
FROM gradle:8.14-jdk17-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración Gradle (para cache)
COPY build.gradle settings.gradle gradlew ./
COPY gradle/ gradle/

# Descargar dependencias
RUN ./gradlew dependencies --no-daemon || return 0

# Copiar código fuente
COPY src/ src/

# Construir el JAR (sin tests)
RUN ./gradlew bootJar --no-daemon -x test

# ===========================
# Etapa 2: Imagen final (runtime)
# ===========================
FROM openjdk:17-jdk-alpine

# Información de la imagen
LABEL maintainer="Equipo de desarrollo"
LABEL description="Microservicio de notificaciones - Arka Project"
LABEL version="1.0.0"

# Crear usuario no-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Instalar herramientas útiles
RUN apk add --no-cache curl

# Directorio de trabajo
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/build/libs/*.jar app.jar

# Cambiar propietario
RUN chown -R appuser:appgroup /app
USER appuser

# Puerto expuesto
EXPOSE 8085

# Variables de entorno
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8085/actuator/health || exit 1

# Comando de ejecución
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
