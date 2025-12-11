package com.apex.trade.Notification_Alerts.trade_execution_alerts.controller;

import com.apex.trade.Notification_Alerts.trade_execution_alerts.dto.TradeEventDTO;
import com.apex.trade.Notification_Alerts.trade_execution_alerts.kafka.producer.TradeProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeController.class)
public class TradeControllerTest {

    @Mock
    private TradeProducer producer;

    @InjectMocks
    private TradeController tradeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    void testExecuteTrade_ShouldPublishMessageAndReturnDTO() throws Exception {

        String requestJson = """
            {
              "orderId": "ORD1001",
              "userId": "USR123",
              "symbol": "AAPL",
              "side": "BUY",
              "type": "MARKET",
              "status": "PENDING",
              "totalQuantity": 50,
              "filledQuantity": 0,
              "avgFillprice": 0.0,
              "timeInForce": "DAY",
              "updatedAt": "2025-09-23T14:30:00"
            }
        """;

        // Perform POST request
        mockMvc.perform(post("/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value("ORD1001"))
                .andExpect(jsonPath("$.symbol").value("AAPL"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Verify producer was called once with any DTO
        verify(producer, times(1)).sendTradeExecutionAlert(any(TradeEventDTO.class));
    }

}