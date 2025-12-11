package com.apex.trade.Notification_Alerts.price_alert.repository;

import com.apex.trade.Notification_Alerts.price_alert.model.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByActiveTrue();
}
