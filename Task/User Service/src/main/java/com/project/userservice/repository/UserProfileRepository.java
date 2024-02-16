package com.project.userservice.repository;

import com.project.userservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public UserProfile executeCustomQuery(Long userId) {
        // Example raw SQL query with a placeholder for userId
        String sqlQuery = "SELECT * FROM user_profiles WHERE user_id = ?";

        try {
            // Execute the query and map the result to a single UserProfile object
            return jdbcTemplate.queryForObject(sqlQuery, new Object[]{userId}, new BeanPropertyRowMapper<>(UserProfile.class));
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no results were found
            return null; // Or throw an exception or handle it based on your requirements
        }
    }

    public List<UserProfile> getAllUsers() {
        String sqlQuery = "SELECT * FROM user_profiles";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(UserProfile.class));
    }
}
