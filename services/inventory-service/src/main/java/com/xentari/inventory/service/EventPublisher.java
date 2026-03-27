package com.xentari.inventory.service;

import com.xentari.inventory.config.RabbitMQConfig;
import com.xentari.inventory.event.StockFailedEvent;
import com.xentari.inventory.event.StockReservedEvent;
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

    public void publishStockReserved(Long orderId, List<StockReservedEvent.ReservedItem> items) {
        String eventId = java.util.UUID.randomUUID().toString();
        StockReservedEvent event = new StockReservedEvent(eventId, orderId, items);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.STOCK_RESERVED_ROUTING_KEY, event);
        log.info("Published stock-reserved for orderId={}, eventId={}", orderId, eventId);
    }

    public void publishStockFailed(Long orderId, Long productId, String reason) {
        String eventId = java.util.UUID.randomUUID().toString();
        StockFailedEvent event = new StockFailedEvent(eventId, orderId, productId, reason);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.STOCK_FAILED_ROUTING_KEY, event);
        log.info("Published stock-failed for orderId={}, eventId={}, reason={}", orderId, eventId, reason);
    }

    public void publishStockRestored(Long orderId, List<StockReservedEvent.ReservedItem> items) {
        String eventId = java.util.UUID.randomUUID().toString();
        StockReservedEvent event = new StockReservedEvent(eventId, orderId, items);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.STOCK_RESTORED_ROUTING_KEY, event);
        log.info("Published stock-restored for orderId={}, eventId={}", orderId, eventId);
    }
}
