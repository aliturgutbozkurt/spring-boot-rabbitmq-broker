package com.turkninja.springbootrabbitmqbroker;

import com.turkninja.springbootrabbitmqbroker.model.Book;
import com.turkninja.springbootrabbitmqbroker.queue.MessageBrokerService;
import com.turkninja.springbootrabbitmqbroker.queue.RabbitMqProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties({RabbitMqProperties.class })
public class SpringBootRabbitmqBrokerApplication implements ApplicationRunner {

    @Autowired
    MessageBrokerService messageBrokerService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRabbitmqBrokerApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        messageBrokerService.send("TestExchange", "testRoutingKey", Book.builder().id(250L).name("Zen and motorcycle").author("Pircing").publishDate("1978").build());
    }
}

