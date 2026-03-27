package com.xentari.order.controller;

import com.xentari.order.dto.OrderResponse;
import com.xentari.order.dto.PlaceOrderRequest;
import com.xentari.order.entity.Order;
import com.xentari.order.event.OrderCreatedEvent;
import com.xentari.order.service.EventPublisher;
import com.xentari.order.service.OrderService;
import com.xentari.order.service.ProductClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order lifecycle management with event-driven saga")
public class OrderController {

    private final OrderService orderService;
    private final EventPublisher eventPublisher;
    private final ProductClient productClient;

    public OrderController(OrderService orderService, EventPublisher eventPublisher, ProductClient productClient) {
        this.orderService = orderService;
        this.eventPublisher = eventPublisher;
        this.productClient = productClient;
    }

    @PostMapping
    @Operation(summary = "Place a new order", description = "Creates an order and triggers the event-driven saga (inventory reservation, payment processing, notification)")
    @ApiResponse(responseCode = "201", description = "Order placed successfully, saga initiated")
    @ApiResponse(responseCode = "400", description = "Invalid order request")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        List<OrderService.OrderItemData> itemDataList = request.items().stream()
                .map(item -> {
                    BigDecimal price = productClient.getProductPrice(item.productId());
                    return new OrderService.OrderItemData(item.productId(), item.quantity(), price);
                })
                .toList();

        Order order = orderService.createOrder(itemDataList);

        List<OrderCreatedEvent.OrderItem> eventItems = order.getItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItem(item.getProductId(), item.getQuantity()))
                .toList();

        eventPublisher.publishOrderCreated(order.getId(), eventItems);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(order));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Returns order details including current status")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    public ResponseEntity<OrderResponse> getOrder(
            @Parameter(description = "Order ID") @PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(toResponse(order));
    }

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns all orders with their current status")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
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
