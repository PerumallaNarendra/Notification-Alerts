package com.apex.trade.Notification_Alerts.trade_execution_alerts.service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendTradeAlertEmail() {

        // Arrange
        String toEmail = "investor@example.com";

        TradeEventDTO dto = new TradeEventDTO();
        dto.setOrderId("ORD1001");
        dto.setSymbol("AAPL");
        dto.setTotalQuantity(50);
        dto.setFilledQuantity(50);
        dto.setAvgFillprice(180.55);
        dto.setStatus(TradeStatus.FULLY_EXECUTED);
        dto.setTimeInForce("DAY");
        dto.setMessage("Your order is complete.");

        // Capture the SimpleMailMessage that gets sent
        ArgumentCaptor<SimpleMailMessage> messageCaptor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.sendTradeAlertEmail(toEmail, dto);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();

        assertEquals(toEmail, sentMessage.getTo()[0]);
        assertEquals("Trade Execution Alert - ORD1001", sentMessage.getSubject());

        String body = sentMessage.getText();

        assertTrue(body.contains("Stock: AAPL"));
        assertTrue(body.contains("Quantity: 50"));
        assertTrue(body.contains("Executed Quantity: 50"));
        assertTrue(body.contains("Price: 180.55"));
        assertTrue(body.contains("Status: FULLY_EXECUTED"));
        assertTrue(body.contains("Timestamp: DAY"));
        assertTrue(body.contains("Note: Your order is complete."));
    }
}

