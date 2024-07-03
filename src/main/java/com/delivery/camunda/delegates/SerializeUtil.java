package com.delivery.camunda.delegates;

import com.delivery.camunda.entity.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SerializeUtil {
    private final ObjectMapper objectMapper;

    public Order converOrder(Object object){

        try {
            var str = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(str, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
