package com.project.userservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaUserProfileProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper for JSON serialization

    public void sendUserProfileEvent(UserProfile userProfile) {
        try {
            String userProfileJson = objectMapper.writeValueAsString(userProfile);
            kafkaTemplate.send("user-profile-topic", String.valueOf(userProfile.getUserId()), userProfileJson);
        } catch (JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
        }
    }
}
