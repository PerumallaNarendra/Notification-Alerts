package com.apex.trade.Notification_Alerts.trade_execution_alerts.service;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeMappingService {

    @Autowired
    private ModelMapper modelMapper;

    public TradeEventDTO convertToDTO(TradeEvent tradeEvent){
        return modelMapper.map(tradeEvent,TradeEventDTO.class);
    }

    public TradeEvent convertToEntity(TradeEventDTO tradeEventDTO){
        return modelMapper.map(tradeEventDTO, TradeEvent.class);
    }
}
