package com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.consumer;

import com.apex.trade.Notification_Alerts.notification_service.NotificationService;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.mock.MockData;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.mock.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TradeConsumer {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MockData mockData;

    @KafkaListener(topics = "trade-execution-alerts", groupId = "alert-group")
    public void consumeTradeAlert(TradeEventDTO tradeEventDTO) {
        System.out.println("Received trade alert for user: " + tradeEventDTO.getUserId());
        // Here we will call Email/SMS service
      Optional<UserData> userData = mockData.fetchUserData()
                .stream()
                .filter(d-> d.getUserId().equals(tradeEventDTO.getUserId()))
                .findFirst();
        userData.ifPresent(data -> notificationService.sendTradeAlertEmail(data.getEmail(), tradeEventDTO));
    }
}
