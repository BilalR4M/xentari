package com.xentari.payment.service;

import com.xentari.payment.config.RabbitMQConfig;
import com.xentari.payment.event.PaymentCompletedEvent;
import com.xentari.payment.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPaymentCompleted(Long orderId, BigDecimal amount) {
        String eventId = java.util.UUID.randomUUID().toString();
        PaymentCompletedEvent event = new PaymentCompletedEvent(eventId, orderId, amount);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.PAYMENT_COMPLETED_ROUTING_KEY, event);
        log.info("Published payment-completed for orderId={}, eventId={}", orderId, eventId);
    }

    public void publishPaymentFailed(Long orderId, String reason) {
        String eventId = java.util.UUID.randomUUID().toString();
        PaymentFailedEvent event = new PaymentFailedEvent(eventId, orderId, reason);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.PAYMENT_FAILED_ROUTING_KEY, event);
        log.info("Published payment-failed for orderId={}, eventId={}, reason={}", orderId, eventId, reason);
    }
}
