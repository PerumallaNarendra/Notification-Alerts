package com.apex.trade.Notification_Alerts.trade_execution_alerts.controller;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.producer.TradeProducer;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.service.TradeMappingService;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeMappingService tradeMappingService;


    private final TradeProducer producer;

    public TradeController(TradeProducer producer) {
        this.producer = producer;
    }

//    @PostMapping("/execute")
//    public String executeTrade(@RequestBody TradeEventDTO eventDTO){
//        TradeEvent tradeEvent = mappingService.convertToEntity(eventDTO);
//        producer.sendTradeExecutionAlert(tradeEvent);
//        return "Trade alert sent";
//    }

    @PostMapping("/execute")
    public ResponseEntity<TradeEventDTO> executeTrade(@RequestBody TradeEventDTO tradeEventDTO) {
        TradeEvent tradeEvent = tradeMappingService.convertToEntity(tradeEventDTO);

        TradeEvent savedTrade = tradeService.executeTrade(tradeEvent);

        TradeEventDTO responseDTO = tradeMappingService.convertToDTO(savedTrade);

        producer.sendTradeExecutionAlert(responseDTO);

        return ResponseEntity.ok(responseDTO);
    }


//    @PostMapping("/test-email")
//    public String testEmail(@RequestBody TradeEvent event) {
//        emailService.sendTradeAlertEmail("investor@example.com", event);
//        return "Email sent!";
//    }

}
