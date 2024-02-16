package com.project.frauddetectionservice.model;

import java.time.LocalDateTime;

public class LikeEvent extends InteractionEvent{
    private Long likeId;
    private Long likerUserId;
    private Long likedUserId;
    private LocalDateTime likeTimestamp;  // Use LocalDateTime for automatic timestamp

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Long getLikerUserId() {
        return likerUserId;
    }

    public void setLikerUserId(Long likerUserId) {
        this.likerUserId = likerUserId;
    }

    public Long getLikedUserId() {
        return likedUserId;
    }

    public void setLikedUserId(Long likedUserId) {
        this.likedUserId = likedUserId;
    }

    public LocalDateTime getLikeTimestamp() {
        return likeTimestamp;
    }

    public void setLikeTimestamp(LocalDateTime likeTimestamp) {
        this.likeTimestamp = likeTimestamp;
    }
}
