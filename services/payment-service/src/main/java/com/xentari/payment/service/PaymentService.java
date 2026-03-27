package com.xentari.payment.service;

import com.xentari.payment.entity.Payment;
import com.xentari.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public boolean processPayment(Long orderId, BigDecimal amount) {
        // Simulate payment gateway — always succeeds for demo purposes
        Payment payment = new Payment(orderId, amount, "COMPLETED");
        paymentRepository.save(payment);
        return true;
    }

    public Optional<Payment> getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
}
