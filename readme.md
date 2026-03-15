# Core

Monolithic backend service for Poomsae Tech platform.

## Tech Stack

- Kotlin 2.2.21
- Spring Boot 4.0.2
- PostgreSQL 17
- Flyway (migrations)
- Spring JDBC (data access)
- MapStruct (DTO mapping)

## Project structure

Используется вариация чистой архитектуры.

```
backend-core/
├─ gradle/ - gradle stuff. Нужен для настройки gradle, лучше не трогать
├─ src/
│  ├─ main/kotlin/ru/poomsae/core/
│  │  ├─ adapter/ - здесь будут лежать все, с помощью чего мы общращаемся к чему-то: клиенты, базы, брокеры и т.д
│  │  │  ├─ postgres/ - например, вот тут будут репозитории для общения с postgres
│  │  ├─ config/ - конфиг приложения
│  │  ├─ domain/ - здесь будут лежать все сущности, которыми будем манипулировать
│  │  ├─ driver/ - здесь будут лежать все, с помощью чего обращаются к нашему сервису: http-роутер, grpc-роутер, сабскрайберы и т.д
│  │  ├─ service/ - здесь лежит основная бизнес-логика
│  │  ├─ CoreApplication.kt - стартовая точка приложения
│  ├─ test/kotlin/ru/poomsae/core/ - тесты
├─ build.gradle.kts - так же файл конфигурации gradle, тут конфигурируются плагины и всякое такое
```

## Prerequisites

- Java 21
- Docker и Docker Compose

## Setup

1. Создать файл `.env` на основе `.env.example`:
   ```bash
   cp .env.example .env
   ```

2. Запустить PostgreSQL:
   ```bash
   docker compose up -d
   ```

3. Запустить приложение:
   ```bash
   ./gradlew bootRun
   ```

## Database Migrations

Flyway миграции находятся в `src/main/resources/db/migration/`.

Создать новую миграцию:
```bash
./migrations.sh <migration_name>
```

## API Endpoints

### Federation

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/federations` | Получить все федерации |
| GET | `/federations/{id}` | Получить федерацию по ID |
| POST | `/federations` | Создать федерацию |
| PUT | `/federations` | Обновить федерацию |
| DELETE | `/federations/{id}` | Удалить федерацию (soft delete) |

## API Documentation

После запуска приложения Swagger UI доступен по адресу:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Configuration

Приложение конфигурируется через переменные окружения (файл `.env`) и `application.yaml`.
