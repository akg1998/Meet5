package com.project.interactionservice.service;

import com.project.interactionservice.model.LikeEvent;
import com.project.interactionservice.model.VisitEvent;
import com.project.interactionservice.repository.LikeInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeInteractionService {

    @Autowired
    public LikeInteractionRepository likeInteractionRepository;

    public String likedUser(LikeEvent likeEvent) {
        return likeInteractionRepository.recordLikeInDatabase(likeEvent);
    }
}
