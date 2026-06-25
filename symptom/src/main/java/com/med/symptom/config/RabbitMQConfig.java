package com.med.symptom.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.routing.key}")
    private String routingKey;
    @Value("${rabbitmq.queue.name}")
    private String queueName;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Bean
    public Queue symptomQueue() {
        return new Queue(queueName,true);
    }

    @Bean
    public DirectExchange symptomExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding symptomBinding(Queue symptomQueue,DirectExchange symptomExchange) {
        return BindingBuilder.bind(symptomQueue).to(symptomExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
