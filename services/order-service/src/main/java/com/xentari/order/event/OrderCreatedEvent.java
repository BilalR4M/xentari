package com.xentari.order.event;

import java.util.List;

public record OrderCreatedEvent(
        String eventId,
        Long orderId,
        List<OrderItem> items
) {
    public record OrderItem(Long productId, Integer quantity) {}
}
