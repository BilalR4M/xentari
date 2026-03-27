package com.xentari.payment.controller;

import com.xentari.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment processing and status")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get payment by order ID", description = "Returns payment status and details for an order")
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found for this order")
    public ResponseEntity<?> getPaymentByOrderId(
            @Parameter(description = "Order ID") @PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId)
                .map(payment -> ResponseEntity.ok(Map.of(
                        "id", payment.getId(),
                        "orderId", payment.getOrderId(),
                        "amount", payment.getAmount(),
                        "status", payment.getStatus(),
                        "createdAt", payment.getCreatedAt()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
