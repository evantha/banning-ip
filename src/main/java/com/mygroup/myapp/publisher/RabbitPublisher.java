package com.mygroup.myapp.publisher;

import io.micronaut.rabbitmq.annotation.Binding;
        import io.micronaut.rabbitmq.annotation.RabbitClient;

@RabbitClient("micronaut")
public interface RabbitPublisher {

    @Binding("analytics")
    void publish(String message);
}