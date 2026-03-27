package com.xentari.payment.event;

public record PaymentFailedEvent(
        String eventId,
        Long orderId,
        String reason
) {}
