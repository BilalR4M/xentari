package com.xentari.payment.controller;

import com.xentari.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getPaymentByOrderId(@PathVariable Long orderId) {
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
