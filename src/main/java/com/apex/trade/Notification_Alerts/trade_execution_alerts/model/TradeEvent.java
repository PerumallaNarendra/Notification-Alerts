package com.apex.trade.Notification_Alerts.trade_execution_alerts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_events")
@Data @AllArgsConstructor @NoArgsConstructor
public class TradeEvent {

    @Id
    private String tradeId;

    private String investorId;
    private String stockSymbol;

    @Enumerated(EnumType.STRING)
    private OrderType orderType; // MARKET / LIMIT

    private int quantity;
    private int executedQuantity;
    private double price;
    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private TradeStatus tradeStatus; // PENDING, PARTIALLY_EXECUTED, FULLY_EXECUTED, FAILED

    private boolean notificationSent = false;

    private LocalDateTime timestamp;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String message;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }



}
