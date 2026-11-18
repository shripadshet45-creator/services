# Account Service

A small Spring Boot microservice for account and money movement operations.

## Run

Requires Java 21 and Maven.

```bash
mvn spring-boot:run
```

The service starts on `http://localhost:8081`.

## API

- `POST /api/accounts` — create an account
- `GET /api/accounts/{id}` — get an account
- `POST /api/accounts/{id}/deposit` — deposit money
- `POST /api/accounts/{id}/withdraw` — withdraw money
- `POST /api/accounts/transfer` — transfer money between accounts
