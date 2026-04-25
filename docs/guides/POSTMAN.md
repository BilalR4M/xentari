# Postman Collection Guide

## Files

| File | Description |
|------|-------------|
| `postman/Xentari.postman_collection.json` | Full API collection with details |
| `postman/Xentari_Local.postman_environment.json` | Local environment variables |

## Import

1. Open Postman Client
2. Click **Import** (top-left)
3. Drag and drop both files or click **Upload Files**
4. Select the **Xentari Local** environment (top-right dropdown)

## Variables

| Variable | Value | Description |
|----------|-------|-------------|
| `baseUrl` | `http://localhost:8080` | API Gateway |
| `productServiceUrl` | `http://localhost:8081` | Direct Product Service |
| `orderServiceUrl` | `http://localhost:8082` | Direct Order Service |
| `inventoryServiceUrl` | `http://localhost:8083` | Direct Inventory Service |
| `paymentServiceUrl` | `http://localhost:8084` | Direct Payment Service |
| `notificationServiceUrl` | `http://localhost:8085` | Direct Notification Service |
| `eurekaUrl` | `http://localhost:8761` | Eureka Dashboard |
| `rabbitmqUrl` | `http://localhost:15672` | RabbitMQ Management |

## Collection Structure

### API Gateway
- Health Check
- Swagger UI

### Product Service
- Get All Products
- Get Product by ID
- Create Product
- Update Product
- Delete Product
- Swagger API Docs

### Order Service
- Place Order (Happy Path)
- Place Order (Stock Failure)
- Get Order by ID
- Get All Orders
- Swagger API Docs

### Inventory Service
- Get Inventory by Product ID
- Swagger API Docs

### Payment Service
- Get Payment by Order ID
- Swagger API Docs

### Infrastructure
- Eureka Dashboard
- RabbitMQ Management
- Eureka Health

### E2E Test Flow
- Step 1: Check Product Catalog
- Step 2: Check Inventory for Product 1
- Step 3: Place Order
- Step 4: Wait for Saga (3s) - Check Order Status
- Step 5: Check Payment Status
- Step 6: Check Inventory After Reservation

## Testing Workflows

### Happy Path Test

1. Run **Step 1: Check Product Catalog** — verify products exist
2. Run **Step 2: Check Inventory for Product 1** — note available quantity
3. Run **Step 3: Place Order** — note the order ID from response
4. Wait 3 seconds for the saga to complete
5. Run **Step 4: Check Order Status** — verify status is `CONFIRMED`
6. Run **Step 5: Check Payment Status** — verify payment is `COMPLETED`
7. Run **Step 6: Check Inventory After Reservation** — verify `reservedQuantity` increased

### Stock Failure Test

1. Run **Place Order (Stock Failure)** — uses quantity 99999
2. Wait 2 seconds
3. Run **Get Order by ID** — verify status is `CANCELLED`

### Create and Order Test

1. Run **Create Product** — note the new product ID
2. Update the order request body with the new product ID
3. Run **Place Order (Happy Path)**
4. Verify the order flow completes

## Pre-Request Scripts

None required. All requests use the `{{baseUrl}}` variable by default.

## Tests

The collection does not include automated test scripts. Use the E2E Test Flow folder for manual verification.

## Troubleshooting

### 503 Service Unavailable

- Ensure all services are running: `docker compose ps`
- Check Eureka dashboard: `http://localhost:8761`
- Wait for all services to register (30-60 seconds after startup)

### Connection Refused

- Verify Docker Compose is running: `docker compose up --build`
- Check port availability: `netstat -an | findstr 8080`

### Order Stays PENDING

- Check RabbitMQ is running: `http://localhost:15672`
- Check service logs: `docker compose logs order-service`
- Verify queues exist in RabbitMQ Management UI
