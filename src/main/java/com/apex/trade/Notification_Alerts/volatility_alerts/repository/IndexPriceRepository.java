package com.apex.trade.Notification_Alerts.volatility_alerts.repository;

import com.apex.trade.Notification_Alerts.volatility_alerts.domain.IndexPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IndexPriceRepository extends JpaRepository<IndexPrice, Long> {

    Optional<IndexPrice> findFirstByIndexNameOrderByPolledAtDesc(String indexName);

    @Query("select p from IndexPrice p where p.indexName = :indexName and p.polledAt >= :from and p.polledAt <= :to order by p.polledAt desc")
    List<IndexPrice> findPricesBetween(String indexName, LocalDateTime from, LocalDateTime to);
}
