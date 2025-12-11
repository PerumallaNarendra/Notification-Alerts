package com.apex.trade.Notification_Alerts.price_alert.service;

import com.apex.trade.Notification_Alerts.notification_service.NotificationService;
import com.apex.trade.Notification_Alerts.price_alert.model.PriceAlert;
import com.apex.trade.Notification_Alerts.price_alert.repository.PriceAlertRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PriceAlertService {

    private final PriceAlertRepository priceAlertRepository;
    private final NotificationService notificationService;

    public PriceAlertService(PriceAlertRepository priceAlertRepository,
                             NotificationService notificationService) {
        this.priceAlertRepository = priceAlertRepository;
        this.notificationService = notificationService;
    }

    public PriceAlert createAlert(PriceAlert alert) {
        return priceAlertRepository.save(alert);
    }

    public void checkPrice(String stockSymbol, double currentPrice) {
        System.out.println("test");
        List<PriceAlert> alerts = priceAlertRepository.findByActiveTrue();

        for (PriceAlert alert : alerts) {
            if (alert.getStockSymbol().equalsIgnoreCase(stockSymbol)) {
                boolean triggered = false;
                switch (alert.getCondition()) {
                    case "GE":
                        triggered = currentPrice >= alert.getTargetPrice();
                        break;
                    case "LE":
                        triggered = currentPrice <= alert.getTargetPrice();
                        break;

                }

                if (triggered) {
                    //notificationService.sendNotification(alert.getInvestorEmail(),"Price Alert triggered for " + stockSymbol +". Current price: " + currentPrice);
                    notificationService.sendStockPriceAlertEmail(
                            alert.getInvestorEmail(),
                            //alert.getInvestorName(),  // or pass some username if you have it
                            stockSymbol,
                            currentPrice,
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                    );
                    //alert.setActive(false);
                    priceAlertRepository.save(alert);
                }
            }
        }
    }
}
