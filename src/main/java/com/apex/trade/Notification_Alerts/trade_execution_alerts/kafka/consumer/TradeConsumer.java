package com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.consumer;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TradeConsumer {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "trade-execution-alerts", groupId = "alert-group")
    public void consumeTradeAlert(TradeEventDTO tradeEventDTO) {
        System.out.println("📩 Received trade alert: " + tradeEventDTO);
        // Here you can call Email/SMS service

        emailService.sendTradeAlertEmail("investor@example.com", tradeEventDTO);
    }
}
