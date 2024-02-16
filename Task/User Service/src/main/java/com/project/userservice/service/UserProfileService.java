package com.project.userservice.service;

import com.project.userservice.model.UserProfile;
import com.project.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile addUser(UserProfile userProfile) {
        return userProfileRepository.addUser(userProfile);
    }
    public UserProfile getUserById(Long userId){
        return userProfileRepository.executeCustomQuery(userId);
    }

    public List<UserProfile> getAllUsers(){
        return userProfileRepository.getAllUsers();
    }

    public Map<String, Object> addBatchOfUsers(List<UserProfile> userProfiles){
        return userProfileRepository.addUsers(userProfiles);
    }
}
