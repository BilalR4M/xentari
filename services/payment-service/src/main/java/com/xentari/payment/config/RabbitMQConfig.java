package com.xentari.payment.config;

public final class RabbitMQConfig {

    public static final String EXCHANGE = "xentari.exchange";

    public static final String STOCK_RESERVED_QUEUE = "payment.inventory.stock-reserved";
    public static final String STOCK_RESERVED_ROUTING_KEY = "inventory.stock-reserved";

    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.payment-completed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.payment-failed";

    private RabbitMQConfig() {}
}
