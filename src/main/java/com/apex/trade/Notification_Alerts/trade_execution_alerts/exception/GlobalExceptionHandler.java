package com.apex.trade.Notification_Alerts.trade_execution_alerts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{


    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleEmailNotFoundException(EmailNotFoundException ex){
        return  new ResponseEntity<>(new ErrorInfo(HttpStatus.NOT_FOUND,ex.getMessage()),HttpStatus.NOT_FOUND);
    }



}
