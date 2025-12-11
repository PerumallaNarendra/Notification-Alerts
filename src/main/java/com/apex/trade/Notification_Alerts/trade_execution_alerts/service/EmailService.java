package com.apex.trade.Notification_Alerts.trade_execution_alerts.service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTradeAlertEmail(String to, TradeEventDTO tradeEventDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Trade Execution Alert - " + tradeEventDTO.getOrderId());

        String text = "Your trade has been executed:\n\n" +
                "Stock: " + tradeEventDTO.getSymbol() + "\n" +
                "Quantity: " + tradeEventDTO.getTotalQuantity() + "\n" +
                "Executed Quantity: " + tradeEventDTO.getFilledQuantity() + "\n" +
                "Price: " + tradeEventDTO.getAvgFillprice() + "\n" +
                "Status: " + tradeEventDTO.getStatus() + "\n" +
                "Timestamp: " + tradeEventDTO.getTimeInForce() + "\n";

        // Optional message
        if (tradeEventDTO.getMessage() != null) {
            text += "\nNote: " + tradeEventDTO.getMessage();
        }

        message.setText(text);

        mailSender.send(message);
    }
}
