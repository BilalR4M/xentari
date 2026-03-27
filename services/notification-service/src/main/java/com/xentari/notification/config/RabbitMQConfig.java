package com.xentari.notification.config;

public final class RabbitMQConfig {

    public static final String EXCHANGE = "xentari.exchange";

    public static final String PAYMENT_COMPLETED_QUEUE = "notification.payment.payment-completed";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.payment-completed";

    public static final String PAYMENT_FAILED_QUEUE = "notification.payment.payment-failed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.payment-failed";

    private RabbitMQConfig() {}
}
