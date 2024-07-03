package com.delivery.camunda.delegates;

import com.delivery.camunda.delegates.enums.Variables;
import com.delivery.camunda.entity.Order;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryDelegate implements JavaDelegate {
    private static final String TOPIC = "inventoryTopic";
    private final Logger LOGGER = LoggerFactory.getLogger(InventoryDelegate.class);
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final SerializeUtil serializeUtil;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var order = serializeUtil.converOrder(delegateExecution.getVariable(Variables.Order.name()));
        order.setProcessId(delegateExecution.getProcessInstanceId());
        LOGGER.info("processing order: {}", order);
        kafkaTemplate.send(TOPIC, order);
    }
}
