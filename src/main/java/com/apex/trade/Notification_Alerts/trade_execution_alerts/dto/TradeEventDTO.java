package com.apex.trade.Notification_Alerts.trade_execution_alerts.dto;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.OrderType;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter @Setter
public class TradeEventDTO {

    private String tradeId;
    private String investorId;
    private String stockSymbol;
    private OrderType orderType;
    private int quantity;
    private int executedQuantity;
    private double price;
    private TradeStatus tradeStatus;
    private LocalDateTime timestamp;

    //optional
    private String message;
}
