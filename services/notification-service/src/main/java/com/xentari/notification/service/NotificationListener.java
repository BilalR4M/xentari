package com.xentari.notification.service;

import com.xentari.notification.config.RabbitMQConfig;
import com.xentari.notification.event.PaymentCompletedEvent;
import com.xentari.notification.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("========================================");
        log.info("NOTIFICATION: Order #{} confirmed!", event.orderId());
        log.info("Amount: ${}", event.amount());
        log.info("Thank you for your purchase!");
        log.info("========================================");
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("========================================");
        log.info("NOTIFICATION: Order #{} has been cancelled.", event.orderId());
        log.info("Reason: {}", event.reason());
        log.info("Please try again or contact support.");
        log.info("========================================");
    }
}
