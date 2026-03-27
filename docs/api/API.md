# API Documentation

Xentari exposes REST APIs through an API Gateway on port 8080. All service endpoints are accessible through the gateway.

## Base URL

```
http://localhost:8080
```

## Services Overview

| Service | Direct Port | Gateway Path | Description |
|---------|-------------|--------------|-------------|
| Product Service | 8081 | `/api/products/**` | Product catalog CRUD |
| Order Service | 8082 | `/api/orders/**` | Order lifecycle management |
| Inventory Service | 8083 | `/api/inventory/**` | Stock reservation |
| Payment Service | 8084 | `/api/payments/**` | Payment processing |

---

## Product Service

### Endpoints

#### `GET /api/products`

Returns all products in the catalog.

**Response:**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "createdAt": "2026-03-27T10:00:00",
    "updatedAt": "2026-03-27T10:00:00"
  }
]
```

**Status Codes:**
- `200 OK` — Products retrieved successfully

---

#### `GET /api/products/{id}`

Returns a single product by ID.

**Path Parameters:**
- `id` (Long) — Product ID

**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99,
  "createdAt": "2026-03-27T10:00:00",
  "updatedAt": "2026-03-27T10:00:00"
}
```

**Status Codes:**
- `200 OK` — Product found
- `404 Not Found` — Product not found

---

#### `POST /api/products`

Creates a new product.

**Request Body:**
```json
{
  "name": "Mechanical Keyboard",
  "description": "RGB backlit mechanical keyboard",
  "price": 149.99
}
```

**Response:**
```json
{
  "id": 6,
  "name": "Mechanical Keyboard",
  "description": "RGB backlit mechanical keyboard",
  "price": 149.99,
  "createdAt": "2026-03-27T12:00:00",
  "updatedAt": "2026-03-27T12:00:00"
}
```

**Status Codes:**
- `201 Created` — Product created successfully
- `400 Bad Request` — Invalid input

---

#### `PUT /api/products/{id}`

Updates an existing product.

**Path Parameters:**
- `id` (Long) — Product ID

**Request Body:**
```json
{
  "name": "Mechanical Keyboard Pro",
  "description": "Updated RGB backlit mechanical keyboard",
  "price": 179.99
}
```

**Status Codes:**
- `200 OK` — Product updated
- `404 Not Found` — Product not found

---

#### `DELETE /api/products/{id}`

Deletes a product.

**Path Parameters:**
- `id` (Long) — Product ID

**Status Codes:**
- `204 No Content` — Product deleted
- `404 Not Found` — Product not found

---

## Order Service

### Endpoints

#### `POST /api/orders`

Places a new order and triggers the event-driven saga.

**Request Body:**
```json
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "status": "PENDING",
  "totalAmount": 10.00,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 10.00
    }
  ],
  "createdAt": "2026-03-27T12:00:00",
  "updatedAt": "2026-03-27T12:00:00"
}
```

**Event Flow After Request:**
1. Order created with status `PENDING`
2. `order.created` event published
3. Inventory Service reserves stock → `STOCK_RESERVED` or `CANCELLED`
4. If stock reserved, Payment Service processes → `CONFIRMED` or `CANCELLED`
5. Notification Service sends confirmation

**Status Codes:**
- `201 Created` — Order placed, saga initiated
- `400 Bad Request` — Invalid order request

---

#### `GET /api/orders/{id}`

Returns order details including current status.

**Path Parameters:**
- `id` (Long) — Order ID

**Response:**
```json
{
  "id": 1,
  "status": "CONFIRMED",
  "totalAmount": 10.00,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "unitPrice": 10.00
    }
  ],
  "createdAt": "2026-03-27T12:00:00",
  "updatedAt": "2026-03-27T12:01:00"
}
```

**Order Status Values:**
- `PENDING` — Order created, awaiting inventory reservation
- `STOCK_RESERVED` — Stock reserved, awaiting payment
- `CONFIRMED` — Payment completed, order confirmed
- `CANCELLED` — Order cancelled (stock failed or payment failed)

**Status Codes:**
- `200 OK` — Order found
- `404 Not Found` — Order not found

---

#### `GET /api/orders`

Returns all orders.

**Response:**
```json
[
  {
    "id": 1,
    "status": "CONFIRMED",
    "totalAmount": 10.00,
    "items": [...],
    "createdAt": "2026-03-27T12:00:00",
    "updatedAt": "2026-03-27T12:01:00"
  }
]
```

**Status Codes:**
- `200 OK` — Orders retrieved

---

## Inventory Service

### Endpoints

#### `GET /api/inventory/{productId}`

Returns stock levels for a product.

**Path Parameters:**
- `productId` (Long) — Product ID

**Response:**
```json
{
  "productId": 1,
  "quantity": 50,
  "reservedQuantity": 2,
  "available": 48
}
```

**Status Codes:**
- `200 OK` — Inventory found
- `404 Not Found` — Product inventory not found

---

## Payment Service

### Endpoints

#### `GET /api/payments/{orderId}`

Returns payment status for an order.

**Path Parameters:**
- `orderId` (Long) — Order ID

**Response:**
```json
{
  "id": 1,
  "orderId": 1,
  "amount": 20.00,
  "status": "COMPLETED",
  "createdAt": "2026-03-27T12:00:30"
}
```

**Status Codes:**
- `200 OK` — Payment found
- `404 Not Found` — Payment not found for this order

---

## Error Responses

All endpoints may return:

```json
{
  "timestamp": "2026-03-27T12:00:00",
  "status": 404,
  "error": "Not Found",
  "path": "/api/products/999"
}
```
