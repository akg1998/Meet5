package com.project.userservice.controller;

import com.project.userservice.model.UserProfile;
import com.project.userservice.service.UserProfileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

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
    public List<UserProfile> getUser(@PathVariable Long userId) {
        return userProfileService.getUserById(userId);
    }

    @GetMapping("/allUsers")
    public List<UserProfile> getAllUsers(){
        return userProfileService.getAllUsers();
    }
}
