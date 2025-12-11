package com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.producer;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TradeProducer {

    private final KafkaTemplate<String, TradeEventDTO> kafkaTemplate;

    public TradeProducer(KafkaTemplate<String, TradeEventDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTradeExecutionAlert(TradeEventDTO tradeEventDTO) {
        kafkaTemplate.send("trade-execution-alerts", tradeEventDTO);
        System.out.println("Sent trade alert to user: " + tradeEventDTO.getUserId());
    }
}
