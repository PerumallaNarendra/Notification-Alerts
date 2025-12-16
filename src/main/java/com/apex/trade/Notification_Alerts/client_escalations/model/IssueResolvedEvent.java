package com.apex.trade.Notification_Alerts.client_escalations.model;

public class IssueResolvedEvent {
    private final Issue issue;

    public IssueResolvedEvent(Issue issue) {
        this.issue = issue;
    }

    public Issue getIssue() { return issue; }
}