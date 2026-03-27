package com.xentari.inventory.event;

public record PaymentFailedEvent(
        String eventId,
        Long orderId,
        String reason
) {}
