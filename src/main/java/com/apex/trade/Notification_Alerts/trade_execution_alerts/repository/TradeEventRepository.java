package com.apex.trade.Notification_Alerts.trade_execution_alerts.repository;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.model.TradeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeEventRepository extends JpaRepository<TradeEvent,String> {
}
