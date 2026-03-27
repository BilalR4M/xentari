package com.xentari.order.config;

public final class RabbitMQConfig {

    public static final String EXCHANGE = "xentari.exchange";

    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    public static final String STOCK_RESERVED_QUEUE = "order.inventory.stock-reserved";
    public static final String STOCK_RESERVED_ROUTING_KEY = "inventory.stock-reserved";

    public static final String STOCK_FAILED_QUEUE = "order.inventory.stock-failed";
    public static final String STOCK_FAILED_ROUTING_KEY = "inventory.stock-failed";

    public static final String PAYMENT_COMPLETED_QUEUE = "order.payment.payment-completed";
    public static final String PAYMENT_COMPLETED_ROUTING_KEY = "payment.payment-completed";

    public static final String PAYMENT_FAILED_QUEUE = "order.payment.payment-failed";
    public static final String PAYMENT_FAILED_ROUTING_KEY = "payment.payment-failed";

    private RabbitMQConfig() {}
}
