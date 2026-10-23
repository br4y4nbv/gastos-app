# ---------- Etapa 1: compilar ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Se copian primero solo los archivos de dependencias. Docker guarda esta capa en caché:
# mientras el pom.xml no cambie, las dependencias no se vuelven a descargar.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests -B

# ---------- Etapa 2: ejecutar ----------
# Imagen final solo con el JRE: más ligera y sin herramientas de compilación
FROM eclipse-temurin:21-jre
WORKDIR /app

# Usuario sin privilegios: si alguien vulnera la app, no tiene acceso de administrador
RUN useradd --system --uid 1001 appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# MaxRAMPercentage hace que la JVM respete el límite de memoria del contenedor
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]