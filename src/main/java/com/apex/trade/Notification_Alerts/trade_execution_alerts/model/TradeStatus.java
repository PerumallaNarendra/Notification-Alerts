package com.apex.trade.Notification_Alerts.trade_execution_alerts.model;

import lombok.Getter;

@Getter
public enum TradeStatus {
    PENDING("Pending execution"),
    PARTIALLY_EXECUTED("Partially executed"),
    FULLY_EXECUTED("Fully executed"),
    FAILED("Failed");

    private final String description;

    TradeStatus(String description) {
        this.description = description;
    }
}
