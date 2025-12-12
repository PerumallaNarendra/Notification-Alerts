package com.apex.trade.Notification_Alerts.volatility_alerts.repository;

import com.apex.trade.Notification_Alerts.volatility_alerts.domain.VolatilityAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VolatilityAlertRepository extends JpaRepository<VolatilityAlert, Long> {

    Optional<VolatilityAlert> findFirstByIndexNameAndAlertTypeOrderByDetectedAtDesc(String indexName, String alertType);
}
