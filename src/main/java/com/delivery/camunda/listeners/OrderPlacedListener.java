package com.delivery.camunda.listeners;

import com.delivery.camunda.delegates.enums.MessageNames;
import com.delivery.camunda.driver.RestDriver;
import com.delivery.camunda.entity.Order;
import lombok.RequiredArgsConstructor;
import org.camunda.community.rest.client.invoker.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderPlacedListener {
    public static final String ORDER_PLACED_TOPIC = "orderPlacedTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(OrderPlacedListener.class);
    private final RestDriver restDriver;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(id = "orderPlaced", topics = ORDER_PLACED_TOPIC, clientIdPrefix = "orderPlacedClientId")
    public void listen(Order data) throws ApiException {
        LOGGER.debug("received message: {}", data);
        if(!restDriver.execute(data.getId(), MessageNames.OrderPlaced.name(), data)){
            kafkaTemplate.send(ORDER_PLACED_TOPIC, data);
        }
    }
}
