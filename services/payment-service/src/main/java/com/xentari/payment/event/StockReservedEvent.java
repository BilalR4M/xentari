package com.xentari.payment.event;

import java.util.List;

public record StockReservedEvent(
        String eventId,
        Long orderId,
        List<ReservedItem> items
) {
    public record ReservedItem(Long productId, Integer quantity) {}
}
