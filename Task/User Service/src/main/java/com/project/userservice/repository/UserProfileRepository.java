package com.project.userservice.repository;

import com.project.userservice.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> addUsers(List<UserProfile> userProfiles) {
        String sqlQuery = "INSERT INTO user_profiles (name, age, gender) VALUES (?, ?, ?)";
        // Create a map to hold the message and inserted user profiles
        Map<String, Object> response = new HashMap<>();
        int[] rowsAffected = jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                UserProfile userProfile = userProfiles.get(i);
                preparedStatement.setString(1, userProfile.getName());
                preparedStatement.setInt(2, userProfile.getAge());
                preparedStatement.setString(3, userProfile.getGender());
            }

            @Override
            public int getBatchSize() {
                return userProfiles.size();
            }
        });

        List<UserProfile> insertedUserProfiles = null;

        for (int i = 0; i < rowsAffected.length; i++) {
            if (rowsAffected[i] > 0) {
                // Fetch the generated user ID for successful insertions
                Long userId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
                userProfiles.get(i).setUserId(userId);

                if (insertedUserProfiles == null) {
                    insertedUserProfiles = new ArrayList<>();
                }
                insertedUserProfiles.add(userProfiles.get(i));
                response.put("message", "All users added to the database successfully");
            }
            else{
                response.put("message", "Some Issue occurred during updation in database");
            }
        }
        response.put("insertedUserProfiles", insertedUserProfiles);
        return response;
    }
}
