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
public class FindDeliveryListener {
    public static final String FIND_DELIVERY_TOPIC = "findDeliveryTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(FindDeliveryListener.class);
    private final RestDriver restDriver;
    private final KafkaTemplate<String, Order> kafkaTemplate;
    @Autowired
    public FindDeliveryListener(RestDriver restDriver, KafkaTemplate<String, Order> kafkaTemplate) {
        this.restDriver = restDriver;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(id = "findDelivery", topics = FIND_DELIVERY_TOPIC, clientIdPrefix = "findDeliveryClientId")
    public void listen(Order data) throws ApiException, InterruptedException {
        LOGGER.info("received message: {}", data);
        //Thread.sleep(1000);;
        if(!restDriver.execute(data.getId(), MessageNames.DeliveryFound.name(), data)){
            kafkaTemplate.send(FIND_DELIVERY_TOPIC, data);
        }
    }
}
