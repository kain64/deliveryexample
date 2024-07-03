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
public class InventoryListener {
    public static final String INVENTORY_TOPIC = "inventoryTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(InventoryListener.class);
    private final RestDriver restDriver;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(id = "inventoryFood", topics = INVENTORY_TOPIC, clientIdPrefix = "inventoryClientId")
    public void listen(Order data) throws ApiException, InterruptedException {

        LOGGER.info("received message: {}", data);
        //Thread.sleep(1000);;
        if(!restDriver.execute(data.getId(), MessageNames.InventoryValidated.name(), data)){
            kafkaTemplate.send(INVENTORY_TOPIC, data);
        }
    }
}
