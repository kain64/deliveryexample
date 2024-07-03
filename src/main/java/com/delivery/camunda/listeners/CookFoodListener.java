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

import java.util.Date;

@Component
public class CookFoodListener {
    public static final String COOK_FOOD_TOPIC = "cookFoodTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(CookFoodListener.class);
    private final RestDriver restDriver;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    @Autowired
    public CookFoodListener(RestDriver restDriver, KafkaTemplate<String, Order> kafkaTemplate) {
        this.restDriver = restDriver;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "cookFood", topics = COOK_FOOD_TOPIC, clientIdPrefix = "cookFoodClientId")
    public void listen(Order data) throws ApiException {
        LOGGER.info("received message: {}", data);
        data.setFoodReady(new Date());
        if(!restDriver.execute(data.getId(), MessageNames.FoodReady.name(), data)){
           kafkaTemplate.send(COOK_FOOD_TOPIC, data);
        }
    }
}
