## Cybersport Platform

Spring Boot REST API для киберспортивной платформы (игры, команды, игроки, матчи, турниры).

## SonarCloud

Результаты анализа качества кода: [SonarCloud](https://sonarcloud.io/project/overview?id=slzz0_cybersport_platform)

Для отправки анализа в SonarCloud:

```bash
mvn clean verify sonar:sonar -Dsonar.token=YOUR_SONAR_TOKEN
```

Если нужен только запуск unit-тестов и генерация покрытия для Sonar:

```bash
mvn clean test
```

XML-отчёт JaCoCo для Sonar будет сгенерирован по пути:

```bash
target/site/jacoco/jacoco.xml
```

## Технологии

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA (Hibernate)
- PostgreSQL
- OpenAPI / Swagger UI (springdoc)
- Maven

## Требования

- Java 21
- Maven (или Maven Wrapper)
- PostgreSQL (локально или удалённо)

## Настройка базы данных (PostgreSQL)

По умолчанию приложение ожидает PostgreSQL на `localhost:5432` и базу `cybersport`.

Настройки находятся в `src/main/resources/application.properties` и могут быть переопределены переменными окружения:

- `SPRING_DATASOURCE_URL` (например `jdbc:postgresql://localhost:5432/cybersport`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Приложение использует `spring.jpa.hibernate.ddl-auto=update`, поэтому таблицы будут создаваться/обновляться автоматически при старте.

## Запуск

Из корня проекта:

```bash
mvn spring-boot:run
```

После запуска приложение доступно по адресу http://localhost:8080.

## Bulk-операция для игроков

В проект добавлена массовая операция создания игроков в рамках одной бизнес-области:

- `POST /api/players/bulk/transactional`
- `POST /api/players/bulk/non-transactional`

Оба эндпоинта принимают список объектов `PlayerRequest`.

Пример запроса:

```json
[
  {
    "nickname": "donk",
    "elo": 3200,
    "teamId": 1
  },
  {
    "nickname": "m0NESY",
    "elo": 3150,
    "teamId": 1
  }
]
```

Для демонстрации транзакционности можно отправить пакет, где один из элементов содержит несуществующий `teamId`.

- `bulk/transactional`: при ошибке откатывается весь пакет
- `bulk/non-transactional`: успешно сохранённые записи остаются в БД, ошибка прерывает обработку только на проблемном элементе

Проект использует PostgreSQL. Тестовый профиль `test` тоже настроен на PostgreSQL и берёт подключение из:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

## Docker

В проект добавлены:

- `frontend/Dockerfile.backend` для backend-приложения
- `frontend/docker-compose.yml` для запуска приложения вместе с PostgreSQL
- `frontend/.env.backend.example` с переменными окружения

### Локальный запуск через Docker Compose

1. Скопируйте переменные окружения:

```bash
cd frontend
cp .env.backend.example .env.backend
```

2. Поднимите контейнеры:

```bash
docker compose --env-file .env.backend up --build
```

3. Проверьте healthcheck:

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/health/readiness
```

### Полезные команды

Остановить контейнеры:

```bash
docker compose down
```

Остановить и удалить volume базы:

```bash
docker compose down -v
```

## Render (бесплатный PaaS)

Для деплоя подготовлен `frontend/render.yaml`.

Что делать:

1. Залейте проект в GitHub.
2. Зарегистрируйтесь на Render.
3. Откройте `Blueprints` -> `New Blueprint Instance`.
4. Выберите ваш GitHub-репозиторий.
5. Укажи путь к blueprint-файлу: `frontend/render.yaml`.
6. Render прочитает его и создаст:
   - бесплатный web service
   - бесплатную PostgreSQL базу
7. Во время первого создания Render попросит значение для `APP_CORS_ALLOWED_ORIGINS`.
   Для frontend можно указать, например:

```text
https://your-frontend-domain.com,http://localhost:5173
```

8. После создания приложение будет доступно по адресу вида:

```text
https://cybersport-platform-api.onrender.com
```

Healthcheck endpoint:

```text
/actuator/health/readiness
```

## GitHub Actions CI/CD

В проект добавлен workflow `.github/workflows/ci-cd.yml`.

Он делает:

- backend build + tests (`mvn verify`)
- frontend build (`npm ci && npm run build`)
- docker build backend-образа
- deploy на Render через deploy hook
- production healthcheck после деплоя

### Какие секреты добавить в GitHub

Откройте `GitHub -> Settings -> Secrets and variables -> Actions` и добавьте:

- `RENDER_DEPLOY_HOOK_URL` — deploy hook из Render
- `APP_BASE_URL` — публичный URL backend, например `https://cybersport-platform-api.onrender.com`

### Как получить deploy hook в Render

1. Откройте ваш web service в Render.
2. Зайдите в `Settings`.
3. Найдите `Deploy Hook`.
4. Скопируйте URL и сохраните его в GitHub Secret `RENDER_DEPLOY_HOOK_URL`.

После этого каждый push в `main` будет:

1. запускать проверки
2. собирать проект
3. триггерить деплой
4. проверять `/actuator/health/readiness`
