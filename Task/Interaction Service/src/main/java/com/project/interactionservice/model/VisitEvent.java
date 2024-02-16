package com.project.interactionservice.model;

import java.sql.Timestamp;


public class VisitEvent {
    public Long getVisitorUserId() {
        return visitorUserId;
    }

    public void setVisitorUserId(Long visitorUserId) {
        this.visitorUserId = visitorUserId;
    }

    public Long getVisitedUserId() {
        return visitedUserId;
    }

    public void setVisitedUserId(Long visitedUserId) {
        this.visitedUserId = visitedUserId;
    }

    private Long visitorUserId;
    private Long visitedUserId;

    private Timestamp visitTimestamp;



    public Timestamp getVisitTimestamp() {
        return visitTimestamp;
    }

    public void setVisitTimestamp(Timestamp visitTimestamp) {
        this.visitTimestamp = visitTimestamp;
    }


}
