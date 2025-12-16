package com.apex.trade.Notification_Alerts.client_escalations.model;

public class IssueEscalatedEvent {
    private final Issue issue;

    public IssueEscalatedEvent(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() { return issue; }
}
