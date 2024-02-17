package com.project.frauddetectionservice.service;

import com.project.frauddetectionservice.model.InteractionEvent;
import com.project.frauddetectionservice.repository.FraudDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class FraudDetectionService {

    @Value("${fraud.threshold.likes}")
    private int likeThreshold;

    @Value("${fraud.threshold.visits}")
    private int visitThreshold;

    @Value("${fraud.threshold.duration}")
    private int timeDuration;

    @Autowired
    private FraudDetectionRepository fraudDetectionRepository;

    // Map to store the timestamp of the last interaction for each user
    private final Map<Long, LocalDateTime> lastInteractionTimestamps = new HashMap<>();

    public void handleInteractionEvent(InteractionEvent interactionEvent) {

        Long userId = interactionEvent.getUserId();
        String eventType = interactionEvent.getEventType();
        LocalDateTime currentTimestamp = LocalDateTime.now();

        if ("visit".equals(eventType)) {
            handleVisit(userId, currentTimestamp);
        } else if ("like".equals(eventType)) {
            handleLike(userId, currentTimestamp);
        }
    }

    private void handleVisit(Long userId, LocalDateTime currentTimestamp) {

        if (lastInteractionTimestamps.containsKey(userId)) {
            LocalDateTime lastVisitTimestamp = lastInteractionTimestamps.get(userId);
            long minutesSinceLastVisit = java.time.Duration.between(lastVisitTimestamp, currentTimestamp).toMinutes();

            if (minutesSinceLastVisit <= timeDuration) {
                // Potential fraudulent activity
                if (getVisitCount(userId) >= visitThreshold) {
                    // Handle fraudulent activity
                    handleFraudulentVisited(userId);
                }
            }
        }
        // Update the last interaction timestamp for the user
        lastInteractionTimestamps.put(userId, currentTimestamp);
    }

    private int getVisitCount(Long userId) {
        return fraudDetectionRepository.getVisitCount(userId);
    }

    private void handleLike(Long userId, LocalDateTime currentTimestamp) {
        if (lastInteractionTimestamps.containsKey(userId)) {
            LocalDateTime lastVisitTimestamp = lastInteractionTimestamps.get(userId);
            long minutesSinceLastVisit = java.time.Duration.between(lastVisitTimestamp, currentTimestamp).toMinutes();

            if (minutesSinceLastVisit <= 10) {
                // Potential fraudulent activity
                if (getLikeCount(userId) >= likeThreshold) {
                    // Handle fraudulent activity
                    handleFraudulentLike(userId);
                }
            }
        }
        // Update the last interaction timestamp for the user
        lastInteractionTimestamps.put(userId, currentTimestamp);
    }

    private int getLikeCount(Long userId) {
        return fraudDetectionRepository.getLikeCount(userId);
    }

    private void handleFraudulentVisited(Long userId) {
        // Logic to handle fraudulent visits, e.g., flagging the user, sending notifications, etc.
        System.out.println("ALERT !!! Fraudulent activity detected: User " + userId + " visited "+visitThreshold+" profiles within a "+timeDuration+" minutes.");
    }

    private void handleFraudulentLike(Long userId) {
        // Logic to handle fraudulent likes, e.g., flagging the user, sending notifications, etc.
        System.out.println("ALERT !!! Fraudulent activity detected: User " + userId + " liked more than "+likeThreshold+" profiles within a "+timeDuration+" minutes.");
    }
}
