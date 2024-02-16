package com.project.interactionservice.repository;

import com.project.interactionservice.kafka.KafkaUserProfileConsumer;
import com.project.interactionservice.model.LikeEvent;
import com.project.interactionservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class LikeInteractionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        String sql = "INSERT INTO user_like (liker_user_id, liked_user_id) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, userProfileVisitor.getUserId(), likeEvent.getLikedUserId());
        if (rowsAffected > 0) {
            UserProfile userProfileLiked = getUserProfileById(likeEvent.getLikedUserId());
            return userProfileVisitor.getName() +" liked profile of "+ userProfileLiked.getName() +" :)";
        } else {
            return "Failed to record the visit.";
        }
    }
}
