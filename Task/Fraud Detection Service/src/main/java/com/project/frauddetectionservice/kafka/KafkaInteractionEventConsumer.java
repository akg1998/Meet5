package com.project.frauddetectionservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.frauddetectionservice.model.InteractionEvent;
import com.project.frauddetectionservice.service.FraudDetectionService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class KafkaInteractionEventConsumer {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON deserialization

    @Autowired
    static InteractionEvent interactionEvent;

    @KafkaListener(topics = "interaction-topic", groupId = "fraud-detection-group")
    public void consumeUserProfileEvent(ConsumerRecord<String, String> record) {
        String interactionJson = record.value();

        try {
            interactionEvent = objectMapper.readValue(interactionJson, InteractionEvent.class);
            // Process the received UserProfile event
            // You can implement the logic to store or update the user profile in your application
            System.out.println("Received UserProfile Event in InteractionService: " + interactionJson);
            fraudDetectionService.handleInteractionEvent(interactionEvent);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

}
