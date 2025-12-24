package com.apex.trade.Notification_Alerts.client_escalations.kafka;

import com.apex.trade.Notification_Alerts.client_escalations.model.Issue;
import com.apex.trade.Notification_Alerts.client_escalations.model.IssueEscalatedEvent;
import com.apex.trade.Notification_Alerts.client_escalations.model.IssueResolvedEvent;
import com.apex.trade.Notification_Alerts.client_escalations.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "onEscalated", groupId = "alert-group")
    public void onEscalated(IssueEscalatedEvent event) {
        Issue issue = event.getIssue();

        String subject = "Escalation Alert: Issue #" + issue.getId() + " ("
                + issue.getPriority() + ")";

        String body = String.format(
                "Dear %s,\n\nA client issue has been escalated:\n\n" +
                        "Client: %s\nContact: %s\nDescription: %s\nPriority: %s\n\n" +
                        "Please provide timely support.\n\n- Notification System",
                issue.getAssignedRep().getName(),
                issue.getClient().getName(),
                issue.getClient().getContact(),
                issue.getDescription(),
                issue.getPriority());

        emailService.sendEmail(issue.getAssignedRep().getEmail(), subject, body);

        System.out.printf("ESCALATION EMAIL sent to %s for issue %d%n",
                issue.getAssignedRep().getEmail(), issue.getId());
    }

    @KafkaListener(topics = "onResolved", groupId = "alert-group")
    public void onResolved(IssueResolvedEvent event) {
        Issue issue = event.getIssue();

        String subject = "Resolution Notice: Issue #" + issue.getId();
        String body = String.format(
                "Dear %s,\n\nThe following client issue has been resolved:\n\n" +
                        "Client: %s\nDescription: %s\n\n" +
                        "Status: RESOLVED\n\n" +
                        "Thank you",
                issue.getAssignedRep().getName(),
                issue.getClient().getName(),
                issue.getDescription());

        String body1 = String.format(
                "Dear %s,\n\nThe following issue is resolved:\n\n" +
                        "Description: %s\n\n" +
                        "Status: RESOLVED\n\n" +
                        "Thank you",
                issue.getClient().getName(),
                issue.getDescription());

        emailService.sendEmail(issue.getAssignedRep().getEmail(), subject, body);
        emailService.sendEmail(issue.getClient().getEmail(), subject, body1);
    }
}
