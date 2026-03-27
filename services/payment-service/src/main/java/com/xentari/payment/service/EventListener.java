package com.xentari.payment.service;

import com.xentari.payment.config.RabbitMQConfig;
import com.xentari.payment.event.StockReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final PaymentService paymentService;
    private final EventPublisher eventPublisher;

    public EventListener(PaymentService paymentService, EventPublisher eventPublisher) {
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESERVED_QUEUE)
    public void handleStockReserved(StockReservedEvent event) {
        log.info("Received inventory.stock-reserved for orderId={}, eventId={}", event.orderId(), event.eventId());

        // Calculate total amount from items (simplified — in reality, would look up product prices)
        // For demo, assume a fixed amount per item or fetch from product service
        BigDecimal amount = BigDecimal.valueOf(event.items().size() * 100);

        boolean success = paymentService.processPayment(event.orderId(), amount);

        if (success) {
            eventPublisher.publishPaymentCompleted(event.orderId(), amount);
        } else {
            eventPublisher.publishPaymentFailed(event.orderId(), "Payment processing failed");
        }
    }
}
