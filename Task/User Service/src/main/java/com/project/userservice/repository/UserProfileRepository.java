package com.project.userservice.repository;

import com.project.userservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserProfile addUser(UserProfile userProfile) {
        String sqlQuery = "INSERT INTO user_profiles (name, age, gender) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sqlQuery, userProfile.getName(), userProfile.getAge(), userProfile.getGender());

        if (rowsAffected > 0) {
            // Fetch the generated user ID (assuming an auto-incremented primary key)
            Long userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
            userProfile.setUserId(userId);
            return userProfile;
        } else {
            return null; // Insertion failed
        }
    }
    public List<UserProfile> executeCustomQuery() {
        // Example raw SQL query
        String sqlQuery = "SELECT * FROM user_profiles";

        // Execute the query and map the results to User objects
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserProfile.class));
    }

    public List<UserProfile> getAllUsers() {
        String sqlQuery = "SELECT * FROM user_profiles";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserProfile.class));
    }
}
