package com.xentari.order.service;

import com.xentari.order.config.RabbitMQConfig;
import com.xentari.order.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOrderCreated(Long orderId, List<OrderCreatedEvent.OrderItem> items) {
        String eventId = java.util.UUID.randomUUID().toString();
        OrderCreatedEvent event = new OrderCreatedEvent(eventId, orderId, items);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ORDER_CREATED_ROUTING_KEY, event);
        log.info("Published order.created for orderId={}, eventId={}", orderId, eventId);
    }
}
