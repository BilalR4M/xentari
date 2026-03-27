package com.xentari.order.event;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String eventId,
        Long orderId,
        BigDecimal amount
) {}
