package com.project.interactionservice.repository;

import com.project.interactionservice.kafka.KafkaUserProfileConsumer;
import com.project.interactionservice.model.UserProfile;
import com.project.interactionservice.model.VisitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class VisitInteractionRepository {

    private JdbcTemplate jdbcTemplate;


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
    public String recordVisitInDatabase(VisitEvent visitEvent) {
        UserProfile userProfileVisitor = KafkaUserProfileConsumer.processUserProfile();
        String sql = "INSERT INTO profile_visit (visitor_user_id, visited_user_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, userProfileVisitor.getUserId(), visitEvent.getVisitedUserId());
        if (rowsAffected > 0) {
            UserProfile userProfileVisited = getUserProfileById(visitEvent.getVisitedUserId());
            return userProfileVisitor.getName() +" visited profile of "+ userProfileVisited.getName() +" :)";
        } else {
            return "Failed to record the visit.";
        }
    }

    // Method to retrieve visits in descending order of timestamps
    public List<VisitEvent> getVisitsForUser(String userId) {
        String sql = "SELECT * FROM profile_visit WHERE visited_user_id = ? ORDER BY visit_timestamp DESC";
        return jdbcTemplate.query(sql, new Object[]{userId}, (resultSet, i) -> {
            VisitEvent visitEvent = new VisitEvent();
            visitEvent.setVisitorUserId(resultSet.getLong("visitor_user_id"));
            visitEvent.setVisitedUserId(resultSet.getLong("visited_user_id"));
            visitEvent.setVisitTimestamp(resultSet.getTimestamp("visit_timestamp"));
            // Other fields as needed...
            return visitEvent;
        });
    }
}
