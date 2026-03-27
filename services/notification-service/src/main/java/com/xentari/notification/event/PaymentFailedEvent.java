package com.xentari.notification.event;

public record PaymentFailedEvent(
        String eventId,
        Long orderId,
        String reason
) {}
