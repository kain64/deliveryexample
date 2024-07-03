package com.delivery.camunda.delegates;

import com.delivery.camunda.delegates.enums.Variables;
import com.delivery.camunda.entity.Order;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliverFoodDelegate  implements JavaDelegate {
    private final Logger LOGGER = LoggerFactory.getLogger(DeliverFoodDelegate.class);
    private static final String TOPIC = "deliveryFoodTopic";
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final SerializeUtil serializeUtil;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var order = serializeUtil.converOrder(delegateExecution.getVariable(Variables.Order.name()));
        LOGGER.info("FOOD DELIVERED: {}", order);
        kafkaTemplate.send(TOPIC, order);
    }
}
