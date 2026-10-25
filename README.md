# Gastos App

Aplicación web de gastos personales con Spring Boot, Thymeleaf y Docker.

![CI](https://github.com/br4y4nbv/gastos-app/actions/workflows/ci.yml/badge.svg)

## Características

- Registro e inicio de sesión con contraseñas cifradas (BCrypt)
- CRUD de gastos con validaciones en el servidor
- Filtros por rango de fechas y categoría, con paginación
- Cada usuario ve únicamente sus propios datos
- Categorías iniciales creadas automáticamente al registrarse

## Tecnologías

- Java 21
- Spring Boot 4 (Web, Data JPA, Security, Validation)
- Thymeleaf + Bootstrap 5
- H2 (desarrollo) y PostgreSQL (producción)
- Maven
- JUnit 5

## Usuario de demostración

Para probar la aplicación sin registrarte:

| Correo | Contraseña |
|---|---|
| demo@gastos.com | demo1234 |

Es una cuenta pública de prueba: no guardes información personal en ella.

## Ejecutar con Docker

Requisitos: Docker y Docker Compose.

```bash
git clone https://github.com/br4y4nbv/gastos-app.git
cd gastos-app
docker compose up --build
```

La aplicación queda en http://localhost:8080 con PostgreSQL y datos persistentes.

Para cambiar las credenciales de la base de datos, copia `.env.example` a `.env` y edítalo.

## Ejecutar en desarrollo (sin Docker)

Requisitos: JDK 21. Usa H2 en memoria: los datos se borran al reiniciar.

```bash
./mvnw spring-boot:run
```

En Windows (PowerShell): `.\mvnw spring-boot:run`

La aplicación queda en http://localhost:8080. Con H2 en memoria, los datos se borran al reiniciar.

## Pruebas

```bash
./mvnw test
```

## Decisiones de diseño

- **`BigDecimal` para los montos**, para evitar errores de redondeo con dinero.
- **DTOs para los formularios** (`GastoForm`, `RegistroForm`), separados de las entidades.
- **Filtrado por usuario en cada consulta**, para que nadie acceda a datos ajenos cambiando un id en la URL.
- **Specifications de Spring Data** para construir filtros opcionales sin escribir consultas largas.
- **`@EntityGraph`** para evitar el problema de consultas N+1 al listar gastos.
- **Patrón Post/Redirect/Get** tras guardar, para que recargar la página no duplique datos.

## Próximos pasos

- [x] Dashboard con totales del mes y gráfico por categoría
- [x] PostgreSQL y Docker Compose
- [x] GitHub Actions
- [ ] Despliegue público
