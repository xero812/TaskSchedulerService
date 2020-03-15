package com.taskschedular.config;

import com.taskschedular.listener.RabbitMQListener;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue}")
    public String QUEUE;

    @Value("${rabbitmq.exchange}")
    public String EXCHANGE;

    @Value("${rabbitmq.topic}")
    public String ROUTING_KEY;

    @Value("${rabbitmq.hostname}")
    public String HOST_NAME;

    @Value("${rabbitmq.username}")
    public String USER_NAME;

    @Value("${rabbitmq.password}")
    public String PASSWORD;

    @Value("${rabbitmq.consumers}")
    public Integer CONSUMER_SIZE;


    @Bean
    Queue myQueue() {
        return new Queue(QUEUE,true);
    }

    @Bean
    Exchange myExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE)
                .durable(true)
                .build();
    }

    @Bean
    Binding myBinding() {
        return BindingBuilder.bind(myQueue())
                .to(myExchange())
                .with(ROUTING_KEY)
                .noargs();
    }

    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(HOST_NAME);
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PASSWORD);
        return connectionFactory;
    }

    @Bean
    MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(myQueue());
        simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
        simpleMessageListenerContainer.setConcurrentConsumers(CONSUMER_SIZE);
        return simpleMessageListenerContainer;
    }

}
