package com.apex.trade.Notification_Alerts.volatility_alerts.service;

import com.apex.trade.Notification_Alerts.notification_service.NotificationService;
import com.apex.trade.Notification_Alerts.volatility_alerts.config.VolatilityProperties;
import com.apex.trade.Notification_Alerts.volatility_alerts.domain.IndexPrice;
import com.apex.trade.Notification_Alerts.volatility_alerts.domain.VolatilityAlert;
import com.apex.trade.Notification_Alerts.volatility_alerts.repository.IndexPriceRepository;
import com.apex.trade.Notification_Alerts.volatility_alerts.repository.VolatilityAlertRepository;
import com.apex.trade.Notification_Alerts.notification_service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class VolatilityServiceTest {

    private IndexPriceRepository priceRepo;
    private VolatilityAlertRepository alertRepo;
    private NotificationService notificationService;
    private VolatilityProperties props;
    private VolatilityService service;

    @BeforeEach
    void setup() {
        priceRepo = mock(IndexPriceRepository.class);
        alertRepo = mock(VolatilityAlertRepository.class);
        notificationService = mock(NotificationService.class);

        props = new VolatilityProperties();
        VolatilityProperties.IndexConfig cfg = new VolatilityProperties.IndexConfig();
        cfg.setName("TEST_INDEX");
        cfg.setApiUrl("http://localhost/fake?symbol={symbol}");
        cfg.setApiSymbol("TEST");
        cfg.setThresholdPercent(1.0);
        cfg.setWindowMinutes(1);
        cfg.setNotifyEmail("user@example.com");
        props.setIndices(List.of(cfg));

        service = new VolatilityService(props, priceRepo, alertRepo, notificationService);
    }

    @Test
    void whenThresholdBreached_andNotAlreadyActive_shouldSendNotification() {
        // Simulate fetchCurrentPrice by spying and stubbing that method to return a value.
        VolatilityService spy = Mockito.spy(service);
//        doReturn(BigDecimal.valueOf(110)).when(spy).(any());

        // earlier price in window = 100
        IndexPrice earlier = new IndexPrice();
        earlier.setIndexName("TEST_INDEX");
        earlier.setPrice(BigDecimal.valueOf(100));
        earlier.setPolledAt(LocalDateTime.now().minusSeconds(30));

        when(priceRepo.findPricesBetween(anyString(), any(), any())).thenReturn(List.of(
                new IndexPrice(), // latest placeholder (will be ignored)
                earlier
        ));
        when(alertRepo.findFirstByIndexNameAndAlertTypeOrderByDetectedAtDesc(anyString(), eq("TRIGGERED")))
                .thenReturn(Optional.empty());

        spy.pollAndEvaluate();

        // verify email send was called
        verify(notificationService, atLeastOnce()).sendStockPriceAlertEmail(eq("user@example.com"), anyString(), anyDouble(), anyString());
        // verify alert saved
        verify(alertRepo, atLeastOnce()).save(any(VolatilityAlert.class));
    }

}
