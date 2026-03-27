package com.xentari.order.service;

import com.xentari.order.config.RabbitMQConfig;
import com.xentari.order.entity.Order;
import com.xentari.order.event.PaymentCompletedEvent;
import com.xentari.order.event.PaymentFailedEvent;
import com.xentari.order.event.StockFailedEvent;
import com.xentari.order.event.StockReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    private final OrderService orderService;

    public EventListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_RESERVED_QUEUE)
    public void handleStockReserved(StockReservedEvent event) {
        log.info("Received inventory.stock-reserved for orderId={}", event.orderId());
        orderService.updateStatus(event.orderId(), "STOCK_RESERVED");
    }

    @RabbitListener(queues = RabbitMQConfig.STOCK_FAILED_QUEUE)
    public void handleStockFailed(StockFailedEvent event) {
        log.info("Received inventory.stock-failed for orderId={}, reason={}", event.orderId(), event.reason());
        orderService.updateStatus(event.orderId(), "CANCELLED");
        log.info("Order {} cancelled due to insufficient stock", event.orderId());
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Received payment.payment-completed for orderId={}", event.orderId());
        orderService.updateStatus(event.orderId(), "CONFIRMED");
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Received payment.payment-failed for orderId={}, reason={}", event.orderId(), event.reason());
        orderService.updateStatus(event.orderId(), "CANCELLED");
        log.info("Order {} cancelled due to payment failure. Stock compensation triggered.", event.orderId());
    }
}
