# Event Flow Documentation

## Overview

Xentari uses a choreography-based event-driven architecture. Services communicate through RabbitMQ using topic exchanges and routing keys.

## RabbitMQ Configuration

### Exchange

| Exchange | Type | Durability |
|----------|------|------------|
| `xentari.exchange` | Topic | Durable |

### Queues and Routing Keys

| Queue | Routing Key | Consumer | Purpose |
|-------|-------------|----------|---------|
| `inventory.order.created` | `order.created` | Inventory Service | Reserve stock for new order |
| `order.inventory.stock-reserved` | `inventory.stock-reserved` | Order Service | Update order to STOCK_RESERVED |
| `order.inventory.stock-failed` | `inventory.stock-failed` | Order Service | Cancel order (insufficient stock) |
| `payment.inventory.stock-reserved` | `inventory.stock-reserved` | Payment Service | Process payment after stock reserved |
| `order.payment.payment-completed` | `payment.payment-completed` | Order Service | Confirm order after payment |
| `order.payment.payment-failed` | `payment.payment-failed` | Order Service | Cancel order (payment failed) |
| `inventory.payment.payment-failed` | `payment.payment-failed` | Inventory Service | Restore stock (compensation) |
| `notification.payment.payment-completed` | `payment.payment-completed` | Notification Service | Send order confirmation |
| `notification.payment.payment-failed` | `payment.payment-failed` | Notification Service | Send cancellation notice |

## Event Payloads

### OrderCreatedEvent

Published when a new order is placed.

```json
{
  "eventId": "uuid",
  "orderId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Routing Key:** `order.created`

---

### StockReservedEvent

Published when stock is successfully reserved.

```json
{
  "eventId": "uuid",
  "orderId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

**Routing Key:** `inventory.stock-reserved`

---

### StockFailedEvent

Published when stock is insufficient.

```json
{
  "eventId": "uuid",
  "orderId": 1,
  "productId": 1,
  "reason": "Insufficient stock"
}
```

**Routing Key:** `inventory.stock-failed`

---

### PaymentCompletedEvent

Published when payment succeeds.

```json
{
  "eventId": "uuid",
  "orderId": 1,
  "amount": 20.00
}
```

**Routing Key:** `payment.payment-completed`

---

### PaymentFailedEvent

Published when payment fails.

```json
{
  "eventId": "uuid",
  "orderId": 1,
  "reason": "Payment processing failed"
}
```

**Routing Key:** `payment.payment-failed`

---

## Saga Flow

### Happy Path

```
Client → API Gateway → Order Service
                              │
                              ├── Save order (PENDING)
                              ├── Publish: order.created
                              │
                              ▼
                        Inventory Service
                              │
                              ├── Reserve stock
                              ├── Publish: inventory.stock-reserved
                              │
                              ▼
                        Payment Service
                              │
                              ├── Process payment
                              ├── Publish: payment.payment-completed
                              │
                              ▼
                  ┌───────────────────────────┐
                  │                           │
                  ▼                           ▼
           Order Service              Notification Service
           (status: CONFIRMED)        (log: "Order confirmed")
```

### Stock Failure Path

```
Inventory Service (insufficient stock)
        │
        ├── Publish: inventory.stock-failed
        │
        ▼
  Order Service
  (status: CANCELLED)
```

### Payment Failure Path (Compensation)

```
Payment Service (payment declined)
        │
        ├── Publish: payment.payment-failed
        │
        ▼
  ┌─────────────────────────────┐
  │                             │
  ▼                             ▼
Order Service              Inventory Service
(status: CANCELLED)        (restore reserved stock)
```

## Failure Handling

### Retry Strategy

- RabbitMQ consumer retries: 3 attempts
- Exponential backoff between retries
- Failed messages go to Dead Letter Queue (if configured)

### Idempotency

- Every event carries a unique `eventId` (UUID)
- Consumers can track processed event IDs to prevent duplicate processing
- Required for safe retries

### Compensation

| Trigger | Compensation |
|---------|-------------|
| `payment.payment-failed` | Inventory restores reserved stock |
| `inventory.stock-failed` | Order marked CANCELLED |

## Message Flow Diagram

```
┌─────────────┐    order.created     ┌─────────────────┐
│ Order       │ ──────────────────►  │ Inventory       │
│ Service     │                      │ Service         │
└─────────────┘                      └────────┬────────┘
       ▲                                      │
       │ stock-reserved/stock-failed          │
       │                                      │
       │                                      ▼
       │                               ┌─────────────────┐
       │                               │ Payment         │
       │                               │ Service         │
       │                               └────────┬────────┘
       │                                        │
       │ payment-completed/payment-failed       │
       │                                        │
       ▼                                        ▼
┌─────────────┐                      ┌─────────────────┐
│ Order       │ ◄────────────────── │ Notification    │
│ Service     │   payment events    │ Service         │
└─────────────┘                      └─────────────────┘
```
