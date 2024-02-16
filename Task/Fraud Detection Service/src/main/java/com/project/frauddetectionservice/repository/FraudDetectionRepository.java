package com.project.frauddetectionservice.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FraudDetectionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int getVisitCount(Long visitorUserId){
        String sql = "SELECT COUNT(*) FROM profile_visit WHERE visitor_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, visitorUserId);
        return count != null ? count : 0;
    }

    public int getLikeCount(Long visitorUserId){
        String sql = "SELECT COUNT(*) FROM user_like WHERE liker_user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, visitorUserId);
        return count != null ? count : 0;
    }
}
