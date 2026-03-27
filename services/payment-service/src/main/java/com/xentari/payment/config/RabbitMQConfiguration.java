package com.xentari.payment.config;

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
    public Queue stockReservedQueue() {
        return new Queue(RabbitMQConfig.STOCK_RESERVED_QUEUE, true);
    }

    @Bean
    public Binding stockReservedBinding(Queue stockReservedQueue, TopicExchange xentariExchange) {
        return BindingBuilder.bind(stockReservedQueue).to(xentariExchange).with(RabbitMQConfig.STOCK_RESERVED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
