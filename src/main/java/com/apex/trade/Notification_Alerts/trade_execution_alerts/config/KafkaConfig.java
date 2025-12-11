package com.apex.trade.Notification_Alerts.trade_execution_alerts.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic tradeExecutionAlertsTopic() {
        return new NewTopic("trade-execution-alerts", 1, (short) 1);
    }
}
