package com.apex.trade.Notification_Alerts.trade_execution_alerts.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.name}")
    private String kafkaTopicName;

    @Bean
    public NewTopic tradeExecutionAlertsTopic() {
        return new NewTopic(kafkaTopicName, 1, (short) 1);
    }
}
