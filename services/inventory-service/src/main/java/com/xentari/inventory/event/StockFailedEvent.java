package com.xentari.inventory.event;

public record StockFailedEvent(
        String eventId,
        Long orderId,
        Long productId,
        String reason
) {}
