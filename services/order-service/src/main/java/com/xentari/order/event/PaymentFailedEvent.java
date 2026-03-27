package com.xentari.order.event;

public record PaymentFailedEvent(
        String eventId,
        Long orderId,
        String reason
) {}
