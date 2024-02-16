package com.project.interactionservice.repository;

import com.project.interactionservice.kafka.KafkaInteractionEventProducer;
import com.project.interactionservice.kafka.KafkaUserProfileConsumer;
import com.project.interactionservice.model.InteractionEvent;
import com.project.interactionservice.model.LikeEvent;
import com.project.interactionservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


@Repository
public class LikeInteractionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public KafkaInteractionEventProducer kafkaInteractionEventProducer;

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

    public String recordLikeInDatabase(LikeEvent likeEvent) {
        UserProfile userProfileVisitor = KafkaUserProfileConsumer.processUserProfile();

        // Insert into interaction_event table
        String insertInteractionEventQuery = "INSERT INTO interaction_event (user_id, event_type, event_timestamp) VALUES (?, ?, NOW())";
        jdbcTemplate.update(insertInteractionEventQuery, userProfileVisitor.getUserId(), "like");

        kafkaInteractionEventProducer.sendInteractionEvent(getInteractionDetails(userProfileVisitor.getUserId()));

        // Retrieve the last generated ID
        Long interactionEventId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        // Insert into user_like table
        String insertUserLikeQuery = "INSERT INTO user_like (interaction_event_id, liker_user_id, liked_user_id) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(insertUserLikeQuery, interactionEventId, userProfileVisitor.getUserId(), likeEvent.getLikedUserId());

        if (rowsAffected > 0) {
            UserProfile userProfileLiked = getUserProfileById(likeEvent.getLikedUserId());
            return userProfileVisitor.getName() +" liked profile of "+ userProfileLiked.getName() +" :)";
        } else {
            return "Failed to record the visit.";
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
