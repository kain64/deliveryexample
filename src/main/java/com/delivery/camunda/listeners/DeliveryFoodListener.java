package com.delivery.camunda.listeners;

import com.delivery.camunda.delegates.enums.MessageNames;
import com.delivery.camunda.driver.RestDriver;
import com.delivery.camunda.entity.Order;
import org.camunda.community.rest.client.invoker.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeliveryFoodListener {
    public static final String DELIVERY_FOOD_TOPIC = "deliveryFoodTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(DeliveryFoodListener.class);
    private final RestDriver restDriver;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    @Autowired
    public DeliveryFoodListener(RestDriver restDriver, KafkaTemplate<String, Order> kafkaTemplate) {
        this.restDriver = restDriver;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(id = "deliveryFood", topics = DELIVERY_FOOD_TOPIC, clientIdPrefix = "deliveryFoodClientId")
    public void listen(Order data) throws ApiException, InterruptedException {
        LOGGER.info("received message: {}", data);

        if(!restDriver.execute(data.getId(), MessageNames.FoodDelivered.name(), data)){
            kafkaTemplate.send(DELIVERY_FOOD_TOPIC, data);
        }
    }
}
