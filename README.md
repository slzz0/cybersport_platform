## Cybersport Platform

Spring Boot REST API для киберспортивной платформы (игры, команды, игроки, матчи, турниры).

## SonarCloud

Результаты анализа качества кода: [SonarCloud](https://sonarcloud.io/project/overview?id=slzz0_cybersport_platform)

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

После запуска приложение доступно по адресу http://localhost:8080.