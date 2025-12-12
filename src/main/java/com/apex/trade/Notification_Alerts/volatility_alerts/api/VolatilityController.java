package com.apex.trade.Notification_Alerts.volatility_alerts.api;

import com.apex.trade.Notification_Alerts.volatility_alerts.domain.VolatilityAlert;
import com.apex.trade.Notification_Alerts.volatility_alerts.repository.VolatilityAlertRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volatility")
public class VolatilityController {

    private final VolatilityAlertRepository alertRepository;

    public VolatilityController(VolatilityAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping("/history")
    public List<VolatilityAlert> history(@RequestParam(required = false) String indexName) {
        List<VolatilityAlert> all = alertRepository.findAll();
        if (indexName == null || indexName.isBlank()) return all;
        return all.stream()
                .filter(a -> a.getIndexName().equalsIgnoreCase(indexName))
                .toList();
    }

    @GetMapping("/latest")
    public VolatilityAlert latest(@RequestParam String indexName) {
        return alertRepository.findFirstByIndexNameAndAlertTypeOrderByDetectedAtDesc(indexName, "TRIGGERED")
                .orElse(null);
    }
}
