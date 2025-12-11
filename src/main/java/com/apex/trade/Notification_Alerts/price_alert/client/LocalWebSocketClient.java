package com.apex.trade.Notification_Alerts.price_alert.client;

import com.apex.trade.Notification_Alerts.price_alert.service.PriceAlertService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class LocalWebSocketClient extends WebSocketClient {

    private final PriceAlertService priceAlertService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocalWebSocketClient(PriceAlertService priceAlertService) throws URISyntaxException {
        super(new URI("ws://localhost:8081/price-feed"));
        this.priceAlertService = priceAlertService;
    }

    @PostConstruct
    public void start() {
        try {
            this.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to local WebSocket server");
        // If your server requires any message to subscribe or authenticate, send here.
        // Otherwise you can remove this send.
    }

    @Override
    public void onMessage(String message) {
        try {
            System.out.println("WebSocket message received: " + message);
            JsonNode root = objectMapper.readTree(message);

            // Parse message depending on your local server message format
            // For example, if your server sends JSON with symbol and price:
            if (root.has("symbol") && root.has("price")) {
                String symbol = root.get("symbol").asText();
                double price = root.get("price").asDouble();

                priceAlertService.checkPrice(symbol, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Local WebSocket closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Local WebSocket error: " + ex.getMessage());
    }
}
