package com.project.interactionservice.service;

import com.project.interactionservice.model.UserProfile;
import com.project.interactionservice.model.VisitEvent;
import com.project.interactionservice.repository.VisitInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisitInteractionService {

    @Autowired
    VisitInteractionRepository visitEventRepository;

    public String visitedUser(VisitEvent visitEvent) {
        return visitEventRepository.recordVisitInDatabase(visitEvent);
    }

    public List<Map<String, Object>> getProfileVisitors() {
        List<Map<String, Object>> rows = visitEventRepository.getProfileVisitors();
        List<Map<String, Object>> result = new ArrayList<>();
        if(!rows.isEmpty()) {
            for (Map<String, Object> row : rows) {
                Long visitorUserId = (Long) row.get("visitor_user_id");
                UserProfile userProfile = visitEventRepository.getUserProfileById(visitorUserId);

                // Assuming UserProfile has getName() method
                String visitorName = userProfile.getName();

                // Add visitorUserId, visitorName, and timestamp to the result map
                row.put("User who visited you profile called as", visitorName);
                row.put("Timestamp", (Timestamp) row.get("timestamp"));

                result.add(row);
            }
            return result;
        }
        else{
            // Create a response for when data is not available
            Map<String, Object> noDataResponse = new HashMap<>();
            noDataResponse.put("message", "No data available for the specified user");

            List<Map<String, Object>> responseList = new ArrayList<>();
            responseList.add(noDataResponse);

            return responseList;
        }
    }
}
