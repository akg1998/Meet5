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

    public List<Map<String, Object>> visitedUser(VisitEvent visitEvent) {
        return visitEventRepository.recordVisitInDatabase(visitEvent);
    }

    public List<Map<String, Object>> getProfileVisitors() {
        List<Map<String, Object>> rows = visitEventRepository.getProfileVisitors();
        List<Map<String, Object>> result = new ArrayList<>();

        if (!rows.isEmpty()) {
            for (Map<String, Object> row : rows) {
                if (row.containsKey("message")) {
                    break;  // This breaks the loop if a row contains the "message" key
                } else {
                    Long visitorUserId = (Long) row.get("visitor_user_id");
                    UserProfile userProfile = visitEventRepository.getUserProfileById(visitorUserId);

                    // Assuming UserProfile has getName() method
                    String visitorName = userProfile.getName();

                    // Add visitorUserId, visitorName, and timestamp to the result map
                    row.put("User who visited you profile called as", visitorName);
                    row.put("Timestamp", (Timestamp) row.get("timestamp"));

                    result.add(row);
                }
            }
        }
        else {
            // Create a response for when data is not available
            Map<String, Object> noDataResponse = new HashMap<>();
            noDataResponse.put("message", "No visitors for this profile yet");

            result.add(noDataResponse);
            return result;
        }
        if(result.isEmpty()){
            return rows;
        }


        return result;
    }
}
