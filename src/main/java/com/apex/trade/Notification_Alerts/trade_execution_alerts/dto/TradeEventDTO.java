package com.apex.trade.Notification_Alerts.trade_execution_alerts.dto;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.OrderType;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter @Getter
public class TradeEventDTO {

//    private String tradeId;
//    private String investorId;
//    private String stockSymbol;
//    private OrderType orderType;
//    private int quantity;
//    private int executedQuantity;
//    private double price;
//    private double totalAmount;
//    private TradeStatus tradeStatus;
//    private LocalDateTime timestamp;

    //Kari's Team DTO

    private String orderId;
    private String userId;
    private String symbol;
    private String side;
    private OrderType type;
    private TradeStatus status;
    private int totalQuantity;
    private int filledQuantity;
    private double avgFillprice;
    private String timeInForce;
    private LocalDateTime updatedAt;


    //optional
    private String message;
}
