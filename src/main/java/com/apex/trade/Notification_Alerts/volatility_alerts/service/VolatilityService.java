package com.apex.trade.Notification_Alerts.volatility_alerts.service;

import com.apex.trade.Notification_Alerts.volatility_alerts.config.VolatilityProperties;
import com.apex.trade.Notification_Alerts.volatility_alerts.domain.IndexPrice;
import com.apex.trade.Notification_Alerts.volatility_alerts.domain.VolatilityAlert;
import com.apex.trade.Notification_Alerts.volatility_alerts.repository.IndexPriceRepository;
import com.apex.trade.Notification_Alerts.volatility_alerts.repository.VolatilityAlertRepository;
import com.apex.trade.Notification_Alerts.notification_service.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Scheduled service that polls configured indices, computes % change over window
 * and triggers TRIGGERED/CLEARED VolatilityAlert events and sends emails using NotificationService.
 * <p>
 * NOTE: apiUrl in configuration must return a JSON that contains a numeric field "price".
 * You can change fetchCurrentPrice(...) to parse the actual API you use.
 */
@Service
public class VolatilityService {

    private final VolatilityProperties properties;
    private final IndexPriceRepository priceRepository;
    private final VolatilityAlertRepository alertRepository;
    private final NotificationService notificationService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${finnhub.api.key:}")
    private String finnhubApiKey;

    public VolatilityService(VolatilityProperties properties,
                             IndexPriceRepository priceRepository,
                             VolatilityAlertRepository alertRepository,
                             NotificationService notificationService) {
        this.properties = properties;
        this.priceRepository = priceRepository;
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
    }


    // scheduled using poll interval (seconds) from properties
    @Scheduled(fixedRateString = "${volatility.poll-interval-seconds:60}000")
    @Transactional
    public void pollAndEvaluate() {
        if (properties.getIndices() == null) return;

        LocalDateTime now = LocalDateTime.now();

        for (VolatilityProperties.IndexConfig cfg : properties.getIndices()) {
            try {
                BigDecimal current = fetchCurrentPrice(cfg);
                if (current == null) continue;

                // save sampled price
                IndexPrice price = new IndexPrice();
                price.setIndexName(cfg.getName());
                price.setPrice(current);
                price.setPolledAt(now);
                priceRepository.save(price);

                // compute window start
                LocalDateTime windowStart = now.minusMinutes(cfg.getWindowMinutes());
                List<IndexPrice> windowPrices = priceRepository.findPricesBetween(cfg.getName(), windowStart, now);
                if (windowPrices.isEmpty()) continue;

                IndexPrice earliest = windowPrices.get(windowPrices.size() - 1);
                BigDecimal earlierPrice = earliest.getPrice();
                if (earlierPrice == null || earlierPrice.compareTo(BigDecimal.ZERO) == 0) continue;

                BigDecimal pctChange = current.subtract(earlierPrice)
                        .divide(earlierPrice, 8, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100));

                BigDecimal threshold = BigDecimal.valueOf(cfg.getThresholdPercent());

                // trigger alert if threshold breached and not already active
                if (pctChange.abs().compareTo(threshold) >= 0) {
                    Optional<VolatilityAlert> lastTriggered = alertRepository
                            .findFirstByIndexNameAndAlertTypeOrderByDetectedAtDesc(cfg.getName(), "TRIGGERED");

                    boolean active = lastTriggered.isPresent() && lastTriggered.get().getResolvedAt() == null;

                    if (!active) {
                        VolatilityAlert alert = new VolatilityAlert();
                        alert.setIndexName(cfg.getName());
                        alert.setAlertType("TRIGGERED");
                        alert.setPercentChange(pctChange);
                        alert.setThreshold(threshold);
                        alert.setWindowMinutes(cfg.getWindowMinutes());
                        alert.setDetectedAt(now);
                        alert.setMessage(String.format("Index %s moved %s%% in last %d minutes",
                                cfg.getName(), pctChange.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString(), cfg.getWindowMinutes()));
                        alert.setNotified(false);
                        alertRepository.save(alert);

                        // send email using existing NotificationService
                        String recipients = cfg.getNotifyEmail() != null ? cfg.getNotifyEmail() : properties.getIndices().get(0).getNotifyEmail();
                        // fallback: use first configured notify email if specific not provided
                        if (recipients == null) recipients = properties.getIndices().get(0).getNotifyEmail();

                        if (recipients != null && !recipients.isBlank()) {
                            for (String to : recipients.split(",")) {
                                notificationService.sendStockPriceAlertEmail(to.trim(), cfg.getName(), current.doubleValue(),
                                        now.toString());
                            }
                        }

                        alert.setNotified(true);
                        alertRepository.save(alert);
                    }
                } else {
                    // if threshold no longer breached, and previously triggered active -> resolve it
                    Optional<VolatilityAlert> lastTriggered = alertRepository
                            .findFirstByIndexNameAndAlertTypeOrderByDetectedAtDesc(cfg.getName(), "TRIGGERED");

                    if (lastTriggered.isPresent() && lastTriggered.get().getResolvedAt() == null) {
                        VolatilityAlert prev = lastTriggered.get();
                        prev.setResolvedAt(now);
                        alertRepository.save(prev);

                        VolatilityAlert cleared = new VolatilityAlert();
                        cleared.setIndexName(cfg.getName());
                        cleared.setAlertType("CLEARED");
                        cleared.setPercentChange(pctChange);
                        cleared.setThreshold(threshold);
                        cleared.setWindowMinutes(cfg.getWindowMinutes());
                        cleared.setDetectedAt(now);
                        cleared.setMessage("Volatility stabilized for " + cfg.getName());
                        cleared.setNotified(false);
                        alertRepository.save(cleared);

                        String recipients = cfg.getNotifyEmail() != null ? cfg.getNotifyEmail() : properties.getIndices().get(0).getNotifyEmail();
                        if (recipients != null && !recipients.isBlank()) {
                            for (String to : recipients.split(",")) {
                                notificationService.sendStockPriceAlertEmail(to.trim(), cfg.getName() + " stabilized", current.doubleValue(),
                                        now.toString());
                            }
                        }

                        cleared.setNotified(true);
                        alertRepository.save(cleared);
                    }
                }

            } catch (Exception ex) {
                System.err.println("Error evaluating volatility for " + cfg.getName() + " : " + ex.getMessage());
                // don't throw; continue with other indices
            }
        }
    }

    /**
     * Fetch current price from configured API.
     * <p>
     * This implementation supports Finnhub as the provider. It expects cfg.getApiSymbol()
     * to contain the ticker/symbol Finnhub recognizes (e.g. ^NSEI, NSE:RELIANCE, AAPL).
     * <p>
     * Finnhub quote endpoint returns JSON with key "c" for current price.
     */
    private BigDecimal fetchCurrentPrice(VolatilityProperties.IndexConfig cfg) {
        try {
            // If no finnhub key configured, fall back to previous generic behavior
            if (finnhubApiKey == null || finnhubApiKey.isBlank()) {
                // fallback: allow cfg.apiUrl style (legacy behavior)
                try {
                    String url = cfg.getApiUrl();
                    if (url == null) return null;
                    if (cfg.getApiSymbol() != null) {
                        url = url.replace("{symbol}", cfg.getApiSymbol());
                    }
                    @SuppressWarnings("unchecked")
                    var resp = restTemplate.getForObject(url, Map.class);
                    if (resp == null) return null;
                    Object priceObj = resp.get("price");
                    if (priceObj == null) {
                        if (resp.containsKey("data") && resp.get("data") instanceof Map) {
                            priceObj = ((Map<?, ?>) resp.get("data")).get("price");
                        }
                    }
                    if (priceObj == null) return null;
                    double val = Double.parseDouble(priceObj.toString());
                    return BigDecimal.valueOf(val);
                } catch (Exception e) {
                    System.err.println("Generic fetch error for " + cfg.getName() + ": " + e.getMessage());
                    return null;
                }
            }

            // Use Finnhub
            String symbol = cfg.getApiSymbol();
            if (symbol == null || symbol.isBlank()) {
                System.err.println("No apiSymbol configured for index " + cfg.getName());
                return null;
            }

            // Build Finnhub quote URL
            String url = UriComponentsBuilder
                    .fromHttpUrl("https://finnhub.io/api/v1/quote")
                    .queryParam("symbol", symbol)
                    .queryParam("token", finnhubApiKey)
                    .toUriString();

            @SuppressWarnings("unchecked")
            Map<String, Object> resp = restTemplate.getForObject(url, Map.class);
            if (resp == null) {
                System.err.println("Empty response from Finnhub for " + cfg.getName());
                return null;
            }

            // Finnhub uses "c" for current price in the quote endpoint
            Object c = resp.get("c");
            if (c == null) {
                // log entire response for diagnostics (only in dev)
                System.err.println("Finnhub response missing 'c' field for " + cfg.getName() + " : " + resp);
                return null;
            }

            double priceVal = Double.parseDouble(c.toString());
            return BigDecimal.valueOf(priceVal);

        } catch (Exception e) {
            System.err.println("Finnhub fetch error for " + cfg.getName() + ": " + e.getMessage());
            return null;
        }
    }
}
