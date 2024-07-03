package com.delivery.camunda;

import com.delivery.camunda.entity.Order;
import net.datafaker.Faker;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Component
public class MsImitationComponent {
    private final KafkaTemplate<String, Order> kafkaTemplate;
    private int counter = 0;

    public MsImitationComponent(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }
    @Scheduled(fixedDelay=5000)
    public void sendDummyOrder(){
        if(counter > 20){
            return;
        }
        counter++;
        Faker faker = new Faker();
        Random random = new Random();
        var rand = 0;
        while (rand ==0){
            rand = random.nextInt(10);
        }
        var foodList = new ArrayList<String>();
        for(int i=0;i<rand;++i){
            foodList.add(faker.food().dish());
        }
        var order = new Order(UUID.randomUUID().toString(), foodList,   faker.address().streetAddress()
                , null, null, null);
        kafkaTemplate.send("orderPlacedTopic", order);
    }
}
