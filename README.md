# Xentari

An event-driven e-commerce microservice system built with Java, Spring Boot, RabbitMQ, PostgreSQL, and Docker.

## Architecture

Xentari is a choreography-based event driven system where services communicate through asynchronous events via RabbitMQ. Each service owns its data, publishes domain events, and reacts to events from other services.

### Services

| Service | Port | Description |
|---------|------|-------------|
| **Eureka Server** | 8761 | Service discovery registry |
| **API Gateway** | 8080 | Single entry point, routing via Eureka |
| **Product Service** | 8081 | Product catalog CRUD |
| **Order Service** | 8082 | Order lifecycle management |
| **Inventory Service** | 8083 | Stock reservation and management |
| **Payment Service** | 8084 | Payment processing |
| **Notification Service** | 8085 | Order confirmation notifications |

### Infrastructure

| Component | Port | Description |
|-----------|------|-------------|
| **PostgreSQL** | 5432 | Database (schema-per-service) |
| **RabbitMQ** | 5672 / 15672 | Message broker (with management UI) |

## Event Flow

```
Order Created ──► Inventory Reserves Stock
                       │
              ┌────────┴────────┐
              ▼                 ▼
        Stock Reserved     Stock Failed
              │                 │
              ▼                 ▼
     Payment Processed    Order Cancelled
              │
       ┌──────┴──────┐
       ▼             ▼
  Payment OK    Payment Failed
       │             │
       ▼             ▼
  Order Confirmed   Stock Restored
       │             Order Cancelled
       ▼
  Notification Sent
```

## Quick Start

### Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

### Run Everything

```bash
docker compose up --build
```

### Access

| URL | Service |
|-----|---------|
| http://localhost:8080 | API Gateway |
| http://localhost:8080/swagger-ui.html | Swagger UI (Aggregated) |
| http://localhost:8761 | Eureka Dashboard |
| http://localhost:15672 | RabbitMQ Management (guest/guest) |

### Place an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"productId": 1, "quantity": 2}
    ]
  }'
```

## API Documentation

### Swagger UI

Interactive API documentation available at:

```
http://localhost:8080/swagger-ui.html
```

### Per-Service API Docs

| Service | API Docs URL |
|---------|-------------|
| Product Service | http://localhost:8080/service/product/v3/api-docs |
| Order Service | http://localhost:8080/service/order/v3/api-docs |
| Inventory Service | http://localhost:8080/service/inventory/v3/api-docs |
| Payment Service | http://localhost:8080/service/payment/v3/api-docs |

### Postman

Import the Postman collection and environment from the `postman/` directory:

- `postman/Xentari.postman_collection.json`
- `postman/Xentari_Local.postman_environment.json`

See [Postman Guide](docs/guides/POSTMAN.md) for details.

## Documentation

- [Getting Started](docs/GETTING-STARTED.md) — Setup and first steps
- [API Reference](docs/api/API.md) — All endpoints with examples
- [Swagger UI Guide](docs/guides/SWAGGER.md) — How to use Swagger UI
- [Postman Guide](docs/guides/POSTMAN.md) — How to use the Postman collection
- [Event Flow](docs/architecture/EVENT-FLOW.md) — Event-driven architecture details
- [Architecture](docs/architecture/ARCHITECTURE.md) — System design and decisions

## Database

Single PostgreSQL instance with schema-per-service isolation:

| Schema | Service |
|--------|---------|
| `product_service` | Product Service |
| `order_service` | Order Service |
| `inventory_service` | Inventory Service |
| `payment_service` | Payment Service |

## Project Structure

```
xentari/
├── docker-compose.yml
├── docs/
│   ├── GETTING-STARTED.md
│   ├── api/
│   │   └── API.md
│   ├── architecture/
│   │   ├── ARCHITECTURE.md
│   │   └── EVENT-FLOW.md
│   └── guides/
│       ├── SWAGGER.md
│       └── POSTMAN.md
├── infrastructure/
│   ├── eureka-server/
│   └── api-gateway/
├── postman/
│   ├── Xentari.postman_collection.json
│   └── Xentari_Local.postman_environment.json
├── scripts/
│   └── init-schemas.sql
└── services/
    ├── product-service/
    ├── order-service/
    ├── inventory-service/
    ├── payment-service/
    └── notification-service/
```

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.x
- **Messaging:** RabbitMQ
- **Database:** PostgreSQL 16
- **Service Discovery:** Spring Cloud Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **API Docs:** SpringDoc OpenAPI 2.8.5
- **Containerization:** Docker + Docker Compose
