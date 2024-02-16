package com.project.interactionservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.interactionservice.model.InteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KafkaInteractionEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON serialization

    private static final String TOPIC = "interaction-topic"; // Replace with your actual Kafka topic

    @Autowired
    public KafkaInteractionEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInteractionEvent(InteractionEvent interactionEvent) {
        try {
            String interactionEventJson = objectMapper.writeValueAsString(interactionEvent);
            kafkaTemplate.send(TOPIC, String.valueOf(interactionEvent.getUserId()), interactionEventJson);
        } catch (JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}
