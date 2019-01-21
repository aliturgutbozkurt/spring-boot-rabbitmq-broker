package com.turkninja.springbootrabbitmqbroker.queue;

public interface MessageBrokerService {

    public void send(String exchangeName, String routingKey, Object message);

}
