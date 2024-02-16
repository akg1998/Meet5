package com.project.frauddetectionservice.model;

import java.sql.Timestamp;


public class VisitEvent extends InteractionEvent{

    private Long visitorUserId;
    private Long visitedUserId;

    private Timestamp visitTimestamp;

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

    public Timestamp getVisitTimestamp() {
        return visitTimestamp;
    }

    public void setVisitTimestamp(Timestamp visitTimestamp) {
        this.visitTimestamp = visitTimestamp;
    }


}
