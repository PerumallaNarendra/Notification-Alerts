package com.apex.trade.Notification_Alerts.volatility_alerts.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Binds properties prefixed with "volatility".
 * Example structure in application.properties shown below.
 */
@Configuration
@ConfigurationProperties(prefix = "volatility")
@Getter @Setter
public class VolatilityProperties {

    /**
     * Poll interval in seconds (how often we fetch current index price)
     */
    private int pollIntervalSeconds = 60;

    /**
     * List of index configs to monitor
     */
    private List<IndexConfig> indices;

    @Getter @Setter
    public static class IndexConfig {
        private String name;           // e.g., NIFTY_50
        private String apiUrl;         // e.g., https://your-price-api/{symbol}
        private String apiSymbol;      // symbol to replace in apiUrl (if applicable)
        private double thresholdPercent = 5.0;
        private int windowMinutes = 30;
        private String notifyEmail;    // optional comma-separated list of emails to notify
    }
}
