CREATE TABLE profile_visit (
    visit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    visitor_user_id BIGINT NOT NULL,
    visited_user_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (visitor_user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (visited_user_id) REFERENCES user_profiles(user_id)
);

CREATE TABLE user_like (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    liker_user_id BIGINT NOT NULL,
    liked_user_id BIGINT NOT NULL,
    like_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (liker_user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (liked_user_id) REFERENCES user_profiles(user_id)
);