package com.turkninja.springbootrabbitmqbroker.queue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMessageBrokerServiceImpl implements MessageBrokerService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {

        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JodaModule());
        OBJECT_MAPPER.enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        OBJECT_MAPPER.disable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    }
    private final RabbitMqProperties rabbitMqProperties;

    private Channel channel;

    private Connection connection = null;

    @PostConstruct
    public void init() {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(rabbitMqProperties.getApi().getHost());
        factory.setPort(rabbitMqProperties.getApi().getPort());
        factory.setUsername(rabbitMqProperties.getApi().getUserName());
        factory.setPassword(rabbitMqProperties.getApi().getPassword());
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        }
        catch (IOException e) {
            log.error("IOException occurred while rabbitMq connection established" + e.getMessage());
        }
        catch (TimeoutException e) {
            log.error("TimeoutException occurred while rabbitMq connection established" + e.getMessage());
        }
    }

    public void send(String exchangeName, String routingKey, Object message) {
        if (exchangeName.isEmpty() || exchangeName == null) {
            throw new IllegalArgumentException("exchangeName cannot be empty while sending message to queue!");
        }

        try {
            String messageJson = OBJECT_MAPPER.writeValueAsString(message);
            channel.basicPublish(exchangeName, routingKey, false,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    messageJson.getBytes());
        } catch (Exception e) {
            log.error(String.format("Cannot send item to queue, name: %s", exchangeName));
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            channel.close();
            connection.close();
        }
        catch (IOException e) {
            log.error("IOException occurred while rabbitMq connection closed" + e.getMessage());
        }
        catch (TimeoutException e) {
            log.error("TimeoutException occurred while rabbitMq connection closed" + e.getMessage());
        }
    }
}

