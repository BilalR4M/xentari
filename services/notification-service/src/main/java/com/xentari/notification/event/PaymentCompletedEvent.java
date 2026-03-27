package com.xentari.notification.event;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String eventId,
        Long orderId,
        BigDecimal amount
) {}
