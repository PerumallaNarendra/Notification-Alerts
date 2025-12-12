package com.apex.trade.Notification_Alerts.volatility_alerts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "volatility_alert")
@Getter @Setter @NoArgsConstructor
public class VolatilityAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_name", nullable = false)
    private String indexName;

    // TRIGGERED or CLEARED
    @Column(name = "alert_type", nullable = false)
    private String alertType;

    @Column(name = "percent_change", precision = 10, scale = 4, nullable = false)
    private BigDecimal percentChange;

    @Column(name = "threshold", precision = 10, scale = 4, nullable = false)
    private BigDecimal threshold;

    @Column(name = "window_minutes", nullable = false)
    private Integer windowMinutes;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "notified", nullable = false)
    private boolean notified = false;
}
