package com.xentari.inventory.config;

public final class RabbitMQConfig {

    public static final String EXCHANGE = "xentari.exchange";

    public static final String ORDER_CREATED_QUEUE = "inventory.order.created";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String PAYMENT_FAILED_QUEUE = "inventory.payment.payment-failed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.payment-failed";

    public static final String STOCK_RESERVED_ROUTING_KEY = "inventory.stock-reserved";
    public static final String STOCK_FAILED_ROUTING_KEY = "inventory.stock-failed";
    public static final String STOCK_RESTORED_ROUTING_KEY = "inventory.stock-restored";

    private RabbitMQConfig() {}
}
