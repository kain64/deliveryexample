package com.delivery.camunda.delegates;

import com.delivery.camunda.delegates.enums.Variables;
import com.delivery.camunda.entity.Order;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CookFoodDelegate  implements JavaDelegate {
    private final Logger LOGGER = LoggerFactory.getLogger(CookFoodDelegate.class);
    private static final String TOPIC = "cookFoodTopic";
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final SerializeUtil serializeUtil;
    @Autowired
    public CookFoodDelegate(KafkaTemplate<String, Order> kafkaTemplate, SerializeUtil serializeUtil) {
        this.kafkaTemplate = kafkaTemplate;
        this.serializeUtil = serializeUtil;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var order = serializeUtil.converOrder(delegateExecution.getVariable(Variables.Order.name()));
        LOGGER.info("processing order: {}", order);
        kafkaTemplate.send(TOPIC, order);
    }
}
