package com.project.interactionservice.controller;

import com.project.interactionservice.model.LikeEvent;
import com.project.interactionservice.model.VisitEvent;
import com.project.interactionservice.service.LikeInteractionService;
import com.project.interactionservice.service.VisitInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class InteractionController {

    @Autowired
    VisitInteractionService visitInteractionService;
    @Autowired
    private LikeInteractionService likeInteractionService;

    @PostMapping("/visit")
    public ResponseEntity<List<Map<String, Object>>> visitUserProfile(@RequestBody VisitEvent visitEvent) {
        List<Map<String, Object>> result = visitInteractionService.visitedUser(visitEvent);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/visitedUsers")
    public ResponseEntity<List<Map<String, Object>>> getProfileVisitors() {
        List<Map<String, Object>> result = visitInteractionService.getProfileVisitors();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<List<Map<String, Object>>> likeUser(@RequestBody LikeEvent likeEvent) {
        List<Map<String, Object>> result = likeInteractionService.likedUser(likeEvent);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
