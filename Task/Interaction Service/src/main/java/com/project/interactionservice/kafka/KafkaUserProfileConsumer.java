package com.project.interactionservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.interactionservice.model.UserProfile;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserProfileConsumer {

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON deserialization
    @Autowired
    static UserProfile userProfileVisitor;

    @KafkaListener(topics = "user-profile-topic", groupId = "user-profile-group")
    public void consumeUserProfileEvent(ConsumerRecord<String, String> record) {
        String userProfileJson = record.value();

        try {
            userProfileVisitor = objectMapper.readValue(userProfileJson, UserProfile.class);
            // Process the received UserProfile event
            // You can implement the logic to store or update the user profile in your application
            System.out.println("Received UserProfile Event in InteractionService: " + userProfileJson);
        } catch (Exception e) {
            // Handle exception
            e.printStackTrace();
        }
    }

    public static UserProfile processUserProfile(){
        return userProfileVisitor;
    }
}
