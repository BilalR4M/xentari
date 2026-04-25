# Getting Started

## Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose

## Quick Start

```bash
# Clone the repository
git clone https://github.com/BilalR4M/xentari.git
cd xentari

# Start all services
docker compose up --build
```

Wait 30-60 seconds for all services to register with Eureka.

## Verify Services

### Eureka Dashboard

```
http://localhost:8761
```

You should see all 5 services registered:
- PRODUCT-SERVICE
- ORDER-SERVICE
- INVENTORY-SERVICE
- PAYMENT-SERVICE
- NOTIFICATION-SERVICE

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Swagger UI

```
http://localhost:8080/swagger-ui.html
```

## Place Your First Order

### Via cURL

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "items": [
      {"productId": 1, "quantity": 2}
    ]
  }'
```

### Via Postman

1. Import `postman/Xentari.postman_collection.json`
2. Import `postman/Xentari_Local.postman_environment.json`
3. Select **Xentari Local** environment
4. Run **E2E Test Flow > Step 3: Place Order**

## Check Order Status

```bash
curl http://localhost:8080/api/orders/1
```

After the saga completes (~2-3 seconds), the status should be `CONFIRMED`.

## Seed Data

The database is pre-populated with:

### Products

| ID | Name | Price |
|----|------|-------|
| 1 | Laptop | $999.99 |
| 2 | Mouse | $29.99 |
| 3 | Keyboard | $79.99 |
| 4 | Monitor | $449.99 |
| 5 | Headphones | $199.99 |

### Inventory

| Product ID | Quantity | Reserved |
|------------|----------|----------|
| 1 | 50 | 0 |
| 2 | 200 | 0 |
| 3 | 100 | 0 |
| 4 | 30 | 0 |
| 5 | 75 | 0 |

## Documentation

- [API Reference](api/API.md) — All endpoints with request/response examples
- [Swagger UI Guide](guides/SWAGGER.md) — How to use Swagger UI
- [Postman Guide](guides/POSTMAN.md) — How to use the Postman collection
- [Event Flow](architecture/EVENT-FLOW.md) — Event-driven architecture details
- [Architecture](architecture/ARCHITECTURE.md) — System design and decisions

## Contributors

- [BilalR4M](https://github.com/BilalR4M)
- IT22277190
- Sudeepa Sandabhanu

### Services not registering with Eureka

- Wait 30-60 seconds after startup
- Check Eureka dashboard: `http://localhost:8761`
- Check service logs: `docker compose logs <service-name>`

### Orders stuck in PENDING

- Check RabbitMQ is running: `http://localhost:15672`
- Verify queues exist in RabbitMQ Management UI
- Check service logs for errors

### Port conflicts

- Ensure ports 5432, 5672, 8080-8085, 8761, 15672 are free
- Stop conflicting services or modify `docker-compose.yml`
