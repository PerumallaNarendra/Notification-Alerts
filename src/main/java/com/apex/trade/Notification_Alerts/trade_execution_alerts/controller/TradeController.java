package com.apex.trade.Notification_Alerts.trade_execution_alerts.controller;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.producer.TradeProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trade")
public class TradeController {


    @Autowired
    private final TradeProducer producer;

    public TradeController(TradeProducer producer) {
        this.producer = producer;
    }


    @PostMapping("/execute")
    public ResponseEntity<TradeEventDTO> executeTrade(@RequestBody TradeEventDTO tradeEventDTO) {

        producer.sendTradeExecutionAlert(tradeEventDTO);

        return ResponseEntity.ok(tradeEventDTO);
    }

}
