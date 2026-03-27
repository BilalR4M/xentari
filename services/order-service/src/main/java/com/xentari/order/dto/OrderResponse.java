package com.xentari.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, String status, BigDecimal totalAmount, List<OrderItemResponse> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
    public record OrderItemResponse(Long productId, Integer quantity, BigDecimal unitPrice) {}
}
