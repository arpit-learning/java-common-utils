# run.md

Local development and verification for agents and automation.

## Startup

```bash
./gradlew bootRun
```

## Health check

- **base_url**: `http://localhost:8080`
- **health_endpoint**: `/actuator/health`

```bash
curl -sf "http://localhost:8080/actuator/health" || echo "service not ready"
```

## Tests

```bash
./gradlew test
```

## Auth

Set required API keys in .env (never commit secrets).
