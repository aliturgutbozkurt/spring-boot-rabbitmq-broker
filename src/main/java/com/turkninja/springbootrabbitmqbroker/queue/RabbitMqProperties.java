package com.turkninja.springbootrabbitmqbroker.queue;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "turkninja.rabbitmq")
public class RabbitMqProperties {

    private Api api = new Api();

    @Data
    public static class Api {

        private String host;
        private int port;
        private String userName;
        private String password;
    }
}
