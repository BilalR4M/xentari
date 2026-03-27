# Architecture Documentation

## System Overview

Xentari is a choreography-based event-driven microservice system for e-commerce. Services communicate asynchronously through RabbitMQ and synchronously through REST APIs via an API Gateway.

## Services

| Service | Port | Database | Events Published | Events Consumed |
|---------|------|----------|------------------|-----------------|
| Eureka Server | 8761 | No | None | None |
| API Gateway | 8080 | No | None | None |
| Product Service | 8081 | Yes | None | None |
| Order Service | 8082 | Yes | `order.created` | `inventory.stock-reserved`, `inventory.stock-failed`, `payment.payment-completed`, `payment.payment-failed` |
| Inventory Service | 8083 | Yes | `inventory.stock-reserved`, `inventory.stock-failed`, `inventory.stock-restored` | `order.created`, `payment.payment-failed` |
| Payment Service | 8084 | Yes | `payment.payment-completed`, `payment.payment-failed` | `inventory.stock-reserved` |
| Notification Service | 8085 | No | None | `payment.payment-completed`, `payment.payment-failed` |

## Infrastructure

| Component | Port | Purpose |
|-----------|------|---------|
| PostgreSQL | 5432 | Database (schema-per-service) |
| RabbitMQ | 5672 / 15672 | Message broker with management UI |

## Database Strategy

Single PostgreSQL instance with schema-per-service isolation:

| Schema | Service | Tables |
|--------|---------|--------|
| `product_service` | Product | `products` |
| `order_service` | Order | `orders`, `order_items` |
| `inventory_service` | Inventory | `inventory` |
| `payment_service` | Payment | `payments` |

### Data Ownership

Each service owns its data exclusively. No service directly accesses another service's schema. Cross-service data access is through events or REST APIs only.

## Technology Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.4.3
- **Service Discovery:** Spring Cloud Netflix Eureka
- **API Gateway:** Spring Cloud Gateway
- **Messaging:** RabbitMQ (Spring AMQP)
- **Database:** PostgreSQL 16
- **ORM:** Spring Data JPA / Hibernate
- **API Docs:** SpringDoc OpenAPI 2.8.5
- **Containerization:** Docker + Docker Compose

## Network Architecture

```
                    ┌─────────────────────────────────────┐
                    │           API Gateway                │
                    │         (port 8080)                  │
                    └──────────────┬──────────────────────┘
                                   │
                    ┌──────────────┼──────────────────────┐
                    │              │                      │
           ┌────────▼───┐  ┌──────▼──────┐  ┌───────────▼───────────┐
           │ Product    │  │ Order       │  │ Inventory             │
           │ Service    │  │ Service     │  │ Service               │
           │ (8081)     │  │ (8082)      │  │ (8083)                │
           └────────────┘  └──────┬──────┘  └───────────┬───────────┘
                                  │                     │
                           ┌──────▼──────┐              │
                           │ Payment     │◄─────────────┘
                           │ Service     │
                           │ (8084)      │
                           └──────┬──────┘
                                  │
                           ┌──────▼──────┐
                           │ Notification│
                           │ Service     │
                           │ (8085)      │
                           └─────────────┘

           ┌─────────────────────────────────────────────┐
           │              RabbitMQ                        │
           │         (port 5672/15672)                    │
           └─────────────────────────────────────────────┘

           ┌─────────────────────────────────────────────┐
           │              PostgreSQL                      │
           │              (port 5432)                     │
           └─────────────────────────────────────────────┘

           ┌─────────────────────────────────────────────┐
           │              Eureka Server                   │
           │              (port 8761)                     │
           └─────────────────────────────────────────────┘
```

## Service Discovery

All services register with Eureka on startup. The API Gateway discovers services through Eureka and routes requests using load-balanced URIs (`lb://SERVICE-NAME`).

### Registration Flow

1. Eureka Server starts (port 8761)
2. Each service includes Eureka client dependency
3. On startup, services register with Eureka
4. Services send heartbeats every 30 seconds
5. Gateway resolves service instances via Eureka

## API Gateway

Spring Cloud Gateway (reactive, Netty-based) provides:

- Single entry point for all client requests
- Eureka-based service discovery routing
- Correlation ID injection (`X-Correlation-Id` header)
- Request logging
- Swagger UI aggregation

### Routes

| Path | Target Service |
|------|---------------|
| `/api/products/**` | PRODUCT-SERVICE |
| `/api/orders/**` | ORDER-SERVICE |
| `/api/inventory/**` | INVENTORY-SERVICE |
| `/api/payments/**` | PAYMENT-SERVICE |
| `/service/product/v3/api-docs` | PRODUCT-SERVICE (Swagger) |
| `/service/order/v3/api-docs` | ORDER-SERVICE (Swagger) |
| `/service/inventory/v3/api-docs` | INVENTORY-SERVICE (Swagger) |
| `/service/payment/v3/api-docs` | PAYMENT-SERVICE (Swagger) |

## Containerization

All services run in Docker containers on a single bridge network (`xentari-network`). Services communicate by container name (e.g., `postgres`, `rabbitmq`, `eureka-server`).

### Docker Compose

```bash
docker compose up --build
```

Starts 10 containers:
- 1 PostgreSQL
- 1 RabbitMQ
- 1 Eureka Server
- 1 API Gateway
- 5 Business Services (Product, Order, Inventory, Payment, Notification)

## Design Decisions

### Why Choreography over Orchestration?

- Simpler implementation (no central coordinator)
- Loose coupling between services
- Each service reacts to events independently
- Easier to add new consumers

### Why Schema-per-Service?

- Data ownership preserved
- Single PostgreSQL instance reduces infrastructure
- No cross-service table access
- Each service can evolve its schema independently

### Why Eureka?

- Dynamic service registration
- Load balancing support
- Health checking
- Demonstrates real microservice patterns for portfolio

### Why Spring Cloud Gateway?

- Single entry point
- Eureka integration
- Reactive (Netty-based)
- Filter chain for cross-cutting concerns
