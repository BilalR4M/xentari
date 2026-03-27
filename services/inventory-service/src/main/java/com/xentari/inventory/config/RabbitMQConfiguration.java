package com.xentari.inventory.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public TopicExchange xentariExchange() {
        return new TopicExchange(RabbitMQConfig.EXCHANGE);
    }

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(RabbitMQConfig.ORDER_CREATED_QUEUE, true);
    }

    @Bean
    public Queue paymentFailedQueue() {
        return new Queue(RabbitMQConfig.PAYMENT_FAILED_QUEUE, true);
    }

    @Bean
    public Binding orderCreatedBinding(Queue orderCreatedQueue, TopicExchange xentariExchange) {
        return BindingBuilder.bind(orderCreatedQueue).to(xentariExchange).with(RabbitMQConfig.ORDER_CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding paymentFailedBinding(Queue paymentFailedQueue, TopicExchange xentariExchange) {
        return BindingBuilder.bind(paymentFailedQueue).to(xentariExchange).with(RabbitMQConfig.PAYMENT_FAILED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
