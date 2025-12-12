package com.apex.trade.Notification_Alerts.trade_execution_alerts.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ErrorInfo {

    private HttpStatus status;
    private String message;
}
