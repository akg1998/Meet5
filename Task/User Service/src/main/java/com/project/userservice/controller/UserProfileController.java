package com.project.userservice.controller;

import com.project.userservice.kafka.KafkaUserProfileProducer;
import com.project.userservice.model.UserProfile;
import com.project.userservice.service.UserProfileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private KafkaUserProfileProducer kafkaUserProfileProducer;

    @PostMapping("/createUser")
    public ResponseEntity<UserProfile> addUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile addedUserProfile = userProfileService.addUser(userProfile);

        if (addedUserProfile != null) {
            return new ResponseEntity<>(addedUserProfile, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{userId}")
    public UserProfile getUser(@PathVariable Long userId) {
        UserProfile userProfileResponse = userProfileService.getUserById(userId);
        // Produce UserProfile event to Kafka
        kafkaUserProfileProducer.sendUserProfileEvent(userProfileResponse);
        return userProfileResponse;
    }

    @GetMapping("/allUsers")
    public List<UserProfile> getAllUsers(){
        return userProfileService.getAllUsers();
    }

    @PostMapping("/bulkDataInsertionOfUsers")
    public  ResponseEntity<Map<String, Object>> addBatchOfUsers(@RequestBody List<UserProfile> userProfiles) {
        Map<String, Object> response = userProfileService.addBatchOfUsers(userProfiles);

        // Return the response with HTTP status OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
