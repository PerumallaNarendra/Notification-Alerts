package com.apex.trade.Notification_Alerts.price_alert.controller;

import com.apex.trade.Notification_Alerts.price_alert.model.PriceAlert;
import com.apex.trade.Notification_Alerts.price_alert.service.PriceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price-alerts")
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping("/create-alert")
    public ResponseEntity<PriceAlert> createAlert(@RequestBody PriceAlert alert) {
        PriceAlert savedAlert = priceAlertService.createAlert(alert);
        System.out.println("hitting api👍");
        return ResponseEntity.ok(savedAlert);
    }

    // Simulate price update endpoint (for testing)
    @PostMapping("/check-price")
    public ResponseEntity<String> checkPrice(@RequestParam String stockSymbol,
                                             @RequestParam double price) {
        priceAlertService.checkPrice(stockSymbol, price);
        return ResponseEntity.ok("Checked price for " + stockSymbol);
    }
}
