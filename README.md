# Task manager

## Create test db

```sql
CREATE DATABASE taskmanagertests WITH OWNER taskmanager ENCODING 'UTF-8' TEMPLATE template0;
```

## Swagger

http://localhost:8080/api/docs


## Docker compose

```bash
docker compose -f ./docker-compose.dev.yml -p task-manager up -d
```
