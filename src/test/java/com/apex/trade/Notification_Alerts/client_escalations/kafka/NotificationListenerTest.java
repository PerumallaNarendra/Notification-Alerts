package com.apex.trade.Notification_Alerts.client_escalations.kafka;

import com.apex.trade.Notification_Alerts.client_escalations.model.*;
import com.apex.trade.Notification_Alerts.client_escalations.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class NotificationListenerTest {

    private EmailService emailService;
    private NotificationListener listener;

    private Issue mockIssue;

    @BeforeEach
    void setup() {
        emailService = Mockito.mock(EmailService.class);
        listener = new NotificationListener(emailService);

        mockIssue = Mockito.mock(Issue.class);
        Client mockClient = Mockito.mock(Client.class);
        ClientServicesRep mockRep = Mockito.mock(ClientServicesRep.class);

        when(mockIssue.getId()).thenReturn(101L);
        when(mockIssue.getDescription()).thenReturn("Test issue description");
        when(mockIssue.getPriority()).thenReturn(Issue.Priority.valueOf("HIGH"));

        when(mockIssue.getClient()).thenReturn(mockClient);
        when(mockIssue.getAssignedRep()).thenReturn(mockRep);

        when(mockClient.getName()).thenReturn("Client A");
        when(mockClient.getContact()).thenReturn("123-456-7890");
        when(mockClient.getEmail()).thenReturn("client@example.com");

        when(mockRep.getName()).thenReturn("John Support");
        when(mockRep.getEmail()).thenReturn("rep@example.com");
    }

    @Test
    void testOnEscalated() {
        IssueEscalatedEvent event = new IssueEscalatedEvent(mockIssue);

        listener.onEscalated(event);

        verify(emailService, times(1)).sendEmail(
                eq("rep@example.com"),
                eq("Escalation Alert: Issue #101 (HIGH)"),
                contains("A client issue has been escalated")
        );
    }

    @Test
    void testOnResolved() {
        IssueResolvedEvent event = new IssueResolvedEvent(mockIssue);

        listener.onResolved(event);

        // 1 email to rep
        verify(emailService, times(1)).sendEmail(
                eq("rep@example.com"),
                eq("Resolution Notice: Issue #101"),
                contains("has been resolved")
        );

        // 2 email to client
        verify(emailService, times(1)).sendEmail(
                eq("client@example.com"),
                eq("Resolution Notice: Issue #101"),
                contains("Status: RESOLVED")
        );
    }
}
