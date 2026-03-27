package com.xentari.order.controller;

import com.xentari.order.dto.OrderResponse;
import com.xentari.order.dto.PlaceOrderRequest;
import com.xentari.order.entity.Order;
import com.xentari.order.event.OrderCreatedEvent;
import com.xentari.order.service.EventPublisher;
import com.xentari.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final EventPublisher eventPublisher;

    public OrderController(OrderService orderService, EventPublisher eventPublisher) {
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        List<OrderService.OrderItemData> itemDataList = request.items().stream()
                .map(item -> new OrderService.OrderItemData(item.productId(), item.quantity(), BigDecimal.TEN))
                .toList();

        Order order = orderService.createOrder(itemDataList);

        List<OrderCreatedEvent.OrderItem> eventItems = order.getItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItem(item.getProductId(), item.getQuantity()))
                .toList();

        eventPublisher.publishOrderCreated(order.getId(), eventItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(toResponse(order));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> responses = orderService.getAllOrders().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(item.getProductId(), item.getQuantity(), item.getUnitPrice()))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), items, order.getCreatedAt(), order.getUpdatedAt());
    }
}
