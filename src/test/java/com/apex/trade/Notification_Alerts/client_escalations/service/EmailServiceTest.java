package com.apex.trade.Notification_Alerts.client_escalations.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void testSendEmail() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService(mailSender);

        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        emailService.sendEmail(to, subject, body);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
