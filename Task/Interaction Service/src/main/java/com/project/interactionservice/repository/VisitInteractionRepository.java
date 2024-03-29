package com.project.interactionservice.repository;

import com.project.interactionservice.kafka.KafkaInteractionEventProducer;
import com.project.interactionservice.kafka.KafkaUserProfileConsumer;
import com.project.interactionservice.model.InteractionEvent;
import com.project.interactionservice.model.UserProfile;
import com.project.interactionservice.model.VisitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VisitInteractionRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public KafkaInteractionEventProducer kafkaInteractionEventProducer;


    @Autowired
    public VisitInteractionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Additional method to retrieve user profile by ID using JDBC
    public UserProfile getUserProfileById(Long userId) {
        String sql = "SELECT * FROM user_profiles WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (resultSet, i) -> {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(resultSet.getLong("user_id"));
            userProfile.setName(resultSet.getString("name"));
            userProfile.setAge(resultSet.getInt("age"));
            userProfile.setGender(resultSet.getString("gender"));
            return userProfile;
        });
    }

    // Additional method to record visit in the database using JDBC
    public List<Map<String, Object>> recordVisitInDatabase(VisitEvent visitEvent) {
        List<Map<String, Object>> responseList = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        try {
            UserProfile userProfileVisitor = KafkaUserProfileConsumer.processUserProfile();

            if (userProfileVisitor == null) {
                // Create a response for when data is not available
                response.put("message", "Error occurred, No data found about user. Please use /user/{userid} get api request to fetch user in Kafka Queue");
                responseList.add(response);
                return responseList;
            }
            // Insert into interaction_event table
            String insertInteractionEventQuery = "INSERT INTO interaction_event (user_id, event_type, event_timestamp) VALUES (?, ?, NOW())";
            jdbcTemplate.update(insertInteractionEventQuery, userProfileVisitor.getUserId(), "visit");

            kafkaInteractionEventProducer.sendInteractionEvent(getInteractionDetails(userProfileVisitor.getUserId()));
            // Retrieve the last generated ID
            Long interactionEventId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

            // Insert into profile_visit table
            String insertUserVisitedQuery = "INSERT INTO profile_visit (interaction_event_id, visitor_user_id, visited_user_id) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(insertUserVisitedQuery, interactionEventId, userProfileVisitor.getUserId(), visitEvent.getVisitedUserId());

            if (rowsAffected > 0) {
                UserProfile userProfileVisited = getUserProfileById(visitEvent.getVisitedUserId());
                response.put("message", userProfileVisitor.getName() + " visited profile of " + userProfileVisited.getName() + " :)");
                responseList.add(response);
                return responseList;
            } else {
                response.put("message", "Failed to record the visit.");
                responseList.add(response);
                return responseList;
            }
        }
        catch (Exception e)
        {
            response.put("message", "Failed to record the visit with exception. "+e);
            responseList.add(response);
            return responseList;
        }
    }

    public List<Map<String, Object>> getProfileVisitors() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> responseList = new ArrayList<>();
        try {
            UserProfile userProfileVisitor = KafkaUserProfileConsumer.processUserProfile();

            if (userProfileVisitor == null) {
                response.put("message", "Error occurred, No data found about user. Please use /user/{userid} get api request to fetch user in Kafka Queue");
                responseList.add(response);
                return responseList;
            }
            String sql = "SELECT visitor_user_id, timestamp FROM profile_visit WHERE visited_user_id = ? ORDER BY timestamp DESC";
            return jdbcTemplate.queryForList(sql, userProfileVisitor.getUserId());
        }
        catch(Exception e){
            response.put("message", "Failed to retrieve visitors with exception. "+e);
            responseList.add(response);
            return responseList;
        }
    }

    public InteractionEvent getInteractionDetails(Long userId) {
        String sql = "SELECT * FROM interaction_event WHERE user_id = ? ORDER BY event_timestamp DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (resultSet, i) -> {
            InteractionEvent interactionEvent = new InteractionEvent();
            interactionEvent.setUserId(resultSet.getLong("user_id"));
            interactionEvent.setEventType(resultSet.getString("event_type"));
            interactionEvent.setEventTimestamp(resultSet.getTimestamp("event_timestamp").toLocalDateTime());
            return interactionEvent;
        });
    }
}
