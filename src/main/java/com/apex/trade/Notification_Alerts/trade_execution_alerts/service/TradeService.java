package com.apex.trade.Notification_Alerts.trade_execution_alerts.service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;

public interface TradeService {

    TradeEvent executeTrade(TradeEvent tradeEvent);
}
