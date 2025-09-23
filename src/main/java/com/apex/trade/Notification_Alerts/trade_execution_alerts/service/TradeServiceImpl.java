package com.apex.trade.Notification_Alerts.trade_execution_alerts.service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.repository.TradeEventRepository;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TradeServiceImpl implements TradeService{

    @Autowired
    private TradeEventRepository tradeEventRepository;

    @Override
    public TradeEvent executeTrade(TradeEvent tradeEvent) {

        if (tradeEvent.getTradeId() == null) {
            tradeEvent.setTradeId(UUID.randomUUID().toString());
        }

        tradeEvent.setExecutedQuantity(tradeEvent.getQuantity());
        tradeEvent.setTotalAmount(tradeEvent.getQuantity()*tradeEvent.getPrice());
        tradeEvent.setTimestamp(LocalDateTime.now());

        return tradeEventRepository.save(tradeEvent);
    }
}
