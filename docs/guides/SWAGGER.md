# Swagger UI Documentation

Xentari uses SpringDoc OpenAPI to generate interactive API documentation with Swagger UI.

## Access Points

### Aggregated Swagger UI (Recommended)

All services are accessible through a single Swagger UI via the API Gateway:

```
http://localhost:8080/swagger-ui.html
```

This UI allows you to browse and test APIs from all services in one place.

### Per-Service Swagger UI

Each service also has its own Swagger UI:

| Service | Swagger UI URL | API Docs URL |
|---------|---------------|--------------|
| Product Service | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| Order Service | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |
| Inventory Service | http://localhost:8083/swagger-ui.html | http://localhost:8083/v3/api-docs |
| Payment Service | http://localhost:8084/swagger-ui.html | http://localhost:8084/v3/api-docs |

### Via API Gateway (Proxied)

API docs are also accessible through the API Gateway:

| Service | Gateway API Docs URL |
|---------|---------------------|
| Product Service | http://localhost:8080/service/product/v3/api-docs |
| Order Service | http://localhost:8080/service/order/v3/api-docs |
| Inventory Service | http://localhost:8080/service/inventory/v3/api-docs |
| Payment Service | http://localhost:8080/service/payment/v3/api-docs |

## Configuration

### Dependencies

All services include:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

### Service Configuration

Each service's `application.yml`:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
```

### API Gateway Configuration

The gateway aggregates all service docs:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: product-service-swagger
          uri: lb://PRODUCT-SERVICE
          predicates:
            - Path=/service/product/v3/api-docs
          filters:
            - SetPath=/v3/api-docs

springdoc:
  swagger-ui:
    urls:
      - url: /service/product/v3/api-docs
        name: Product Service
      - url: /service/order/v3/api-docs
        name: Order Service
      - url: /service/inventory/v3/api-docs
        name: Inventory Service
      - url: /service/payment/v3/api-docs
        name: Payment Service
```

## Annotations Used

### Controller-Level

```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product catalog management")
public class ProductController { ... }
```

### Method-Level

```java
@Operation(summary = "Create a new product", description = "Adds a new product to the catalog")
@ApiResponse(responseCode = "201", description = "Product created successfully")
@ApiResponse(responseCode = "400", description = "Invalid input")
public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) { ... }
```

### Parameter-Level

```java
public ResponseEntity<ProductResponse> getProductById(
    @Parameter(description = "Product ID") @PathVariable Long id) { ... }
```

## Testing via Swagger UI

1. Open `http://localhost:8080/swagger-ui.html`
2. Select a service from the dropdown (top-right)
3. Expand an endpoint
4. Click "Try it out"
5. Fill in parameters (if required)
6. Click "Execute"
7. View the response below

## Prerequisites

All services must be running:

```bash
docker compose up --build
```

Wait for all services to register with Eureka (check `http://localhost:8761`).
