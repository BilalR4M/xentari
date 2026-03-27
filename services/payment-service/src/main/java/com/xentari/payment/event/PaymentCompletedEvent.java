package com.xentari.payment.event;

public record PaymentCompletedEvent(
        String eventId,
        Long orderId,
        java.math.BigDecimal amount
) {}
