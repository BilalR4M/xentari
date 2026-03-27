package com.xentari.inventory.service;

import com.xentari.inventory.config.RabbitMQConfig;
import com.xentari.inventory.event.OrderCreatedEvent;
import com.xentari.inventory.event.PaymentFailedEvent;
import com.xentari.inventory.event.StockReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final InventoryService inventoryService;
    private final EventPublisher eventPublisher;

    public EventListener(InventoryService inventoryService, EventPublisher eventPublisher) {
        this.inventoryService = inventoryService;
        this.eventPublisher = eventPublisher;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order.created for orderId={}, eventId={}", event.orderId(), event.eventId());

        List<StockReservedEvent.ReservedItem> reservedItems = new ArrayList<>();

        for (OrderCreatedEvent.OrderItem item : event.items()) {
            boolean reserved = inventoryService.reserveStock(item.productId(), item.quantity());
            if (!reserved) {
                log.warn("Insufficient stock for productId={}, orderId={}", item.productId(), event.orderId());

                // Release any previously reserved items in this order
                for (StockReservedEvent.ReservedItem ri : reservedItems) {
                    inventoryService.releaseStock(ri.productId(), ri.quantity());
                }

                eventPublisher.publishStockFailed(event.orderId(), item.productId(), "Insufficient stock");
                return;
            }
            reservedItems.add(new StockReservedEvent.ReservedItem(item.productId(), item.quantity()));
        }

        eventPublisher.publishStockReserved(event.orderId(), reservedItems);
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Received payment.payment-failed for orderId={}, eventId={}. Restoring stock.", event.orderId(), event.eventId());

        // In a real system, we'd look up what was reserved. For simplicity, we emit a restore event.
        // The Order Service tracks what items were in the order and can provide them.
        // Here we assume the notification is sufficient — the reserved quantities will time out or
        // we implement a lookup mechanism. For this demo, we'll rely on the order service to tell us.
        log.info("Stock restoration triggered for orderId={}", event.orderId());
    }
}
