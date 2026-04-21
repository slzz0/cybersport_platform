## Frontend Infra Notes

В этой папке лежат frontend-файлы и связанная инфраструктура для деплоя backend:

- `Dockerfile`
- `Dockerfile.backend`
- `docker-compose.yml`
- `.env.backend.example`
- `render.yaml`
- `nginx.conf`
- `docker/entrypoint.sh`

Локальный запуск:

```bash
cd /home/slzzxd/cybersport_platform/frontend
cp .env.backend.example .env.backend
docker compose --env-file .env.backend up --build
```

Что поднимется одной командой:

- `frontend` на `http://localhost:5173`
- `backend` на `http://localhost:8080`
- `postgres` на `localhost:${POSTGRES_PORT}`

Healthcheck:

```bash
curl http://localhost:5173
curl http://localhost:8080/actuator/health/readiness
```
