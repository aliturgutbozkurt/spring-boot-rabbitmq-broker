package com.turkninja.springbootrabbitmqbroker.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("turkninja.rabbitmq")
public class RabbitMqConfig {

    @Autowired
    private RabbitMqProperties properties;

    @Bean
    public MessageBrokerService messageBrokerService() {
        return new RabbitMessageBrokerServiceImpl(properties);
    }
}
