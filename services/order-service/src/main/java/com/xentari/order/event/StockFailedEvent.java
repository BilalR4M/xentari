package com.xentari.order.event;

public record StockFailedEvent(
        String eventId,
        Long orderId,
        Long productId,
        String reason
) {}
