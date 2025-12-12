package com.apex.trade.Notification_Alerts.notification_service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String to, String message) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);
        mail.setSubject("Notification");
        mail.setText(message);
        mailSender.send(mail);
        System.out.println("Email sent to " + to);

        System.out.println("Sending notification to " + to + ": " + message);
    }
    public void sendStockPriceAlertEmail(String to, String stockSymbol, double price, String dateTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Stock Price Alert: " + stockSymbol + " reached " + price);

            String htmlContent = "<html><body>" +
                    "<h3>Stock Price Alert: " + stockSymbol + " has reached " + price + "</h3>" +
                    "<p>Hello ,</p>" +
                    "<p>The stock <strong>" + stockSymbol + "</strong> you’re tracking has reached your target price of <strong>" + price + "</strong>.</p>" +
                    "<p>Alert Time: " + dateTime + "</p>" +
                    "<p>Thank you for using our service!</p>" +
                    "</body></html>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println("HTML email sent to " + to);

        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
            // Handle exception properly, maybe throw a custom exception or log
        }
    }
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
