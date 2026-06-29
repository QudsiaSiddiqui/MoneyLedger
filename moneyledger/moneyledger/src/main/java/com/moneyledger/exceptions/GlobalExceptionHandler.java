package com.moneyledger.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            InsufficientFundsException.class
    )
    public ResponseEntity<?> handleFunds(
            InsufficientFundsException ex
    ) {

        return ResponseEntity
                .status(
                        HttpStatus.BAD_REQUEST
                )
                .body(
                        Map.of(
                                "timestamp",
                                LocalDateTime.now(),
                                "error",
                                ex.getMessage()
                        )
                );
    }

}