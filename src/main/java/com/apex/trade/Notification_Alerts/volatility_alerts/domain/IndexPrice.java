package com.apex.trade.Notification_Alerts.volatility_alerts.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "index_price")
@Getter @Setter @NoArgsConstructor
public class IndexPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index_name", nullable = false)
    private String indexName;

    @Column(name = "price", nullable = false, precision = 20, scale = 8)
    private BigDecimal price;

    @Column(name = "polled_at", nullable = false)
    private LocalDateTime polledAt;
}
