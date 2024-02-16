package com.project.interactionservice.service;

import com.project.interactionservice.model.VisitEvent;
import com.project.interactionservice.repository.VisitInteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitInteractionService {

    @Autowired
    VisitInteractionRepository visitEventRepository;

    public String visitedUser(VisitEvent visitEvent) {
        return visitEventRepository.recordVisitInDatabase(visitEvent);
    }
}
