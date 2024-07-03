package com.delivery.camunda.driver;


import com.delivery.camunda.entity.Order;
import lombok.RequiredArgsConstructor;
import org.camunda.community.rest.client.api.EventSubscriptionApi;
import org.camunda.community.rest.client.api.MessageApi;
import org.camunda.community.rest.client.dto.CorrelationMessageDto;
import org.camunda.community.rest.client.dto.VariableValueDto;
import org.camunda.community.rest.client.invoker.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestDriver {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestDriver.class);
    private final MessageApi messageApi;
    private final EventSubscriptionApi eventSubscriptionApi;


    private void execute(CorrelationMessageDto correlationMessageDto) throws ApiException {
        messageApi.deliverMessage(correlationMessageDto);
    }

    public boolean isMessageCorrelated(
            String eventName,
            String processId) throws ApiException {
        if (processId == null) {
            return true;
        }
        var res = eventSubscriptionApi.getEventSubscriptions(null,
                eventName, "message", null, processId,
                null, null, null, null, null, null, null, null);
        return res != null && res.size() == 1;
    }

    //    public VariableValueDto serializeObjectToVariableValueDto(Object object) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonString = objectMapper.writeValueAsString(object);
//
//            String serializationDataFormat = "application/json;" + "objectTypeName=" + object.getClass();
//
//            ObjectValue objectValue = Variables
//                    .objectValue(jsonString)
//                    .serializationDataFormat(serializationDataFormat)
//                    .create();
//
//            TypedValue typedValue = Variables
//                    .objectValue(objectValue)
//                    .serializationDataFormat(Variables.SerializationDataFormats.JSON)
//                    .create();
//
//            VariableValueDto variableValueDto = new VariableValueDto();
//            variableValueDto.setValue(typedValue);
//
//            return variableValueDto;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
    public boolean execute(String businessKey, String messageName, Order data) throws ApiException {
        if (!isMessageCorrelated(messageName, data.getProcessId())) {
            return false;
        }
        LOGGER.debug("message delivering: {}, {}, {}", businessKey, messageName, data);
        CorrelationMessageDto correlationMessageDto = new CorrelationMessageDto();
        correlationMessageDto.setBusinessKey(businessKey);
        correlationMessageDto.setMessageName(messageName);
        Map<String, VariableValueDto> vars = new HashMap<>();
        VariableValueDto variableValueDto = new VariableValueDto();
        variableValueDto.setValue(data);
        vars.put(com.delivery.camunda.delegates.enums.Variables.Order.name(), variableValueDto);
        correlationMessageDto.setProcessVariables(vars);
        execute(correlationMessageDto);
        return true;
    }
}
