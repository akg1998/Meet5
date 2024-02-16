CREATE TABLE profile_visit (
    visit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interaction_event_id BIGINT NOT NULL,
    visitor_user_id BIGINT NOT NULL,
    visited_user_id BIGINT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (interaction_event_id) REFERENCES interaction_event(event_id),
    FOREIGN KEY (visitor_user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (visited_user_id) REFERENCES user_profiles(user_id)
);

CREATE TABLE user_like (
    like_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    interaction_event_id BIGINT NOT NULL,
    liker_user_id BIGINT NOT NULL,
    liked_user_id BIGINT NOT NULL,
    like_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (interaction_event_id) REFERENCES interaction_event(event_id),
    FOREIGN KEY (liker_user_id) REFERENCES user_profiles(user_id),
    FOREIGN KEY (liked_user_id) REFERENCES user_profiles(user_id)
);

CREATE TABLE interaction_event (
    event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    event_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_profiles(user_id)
);