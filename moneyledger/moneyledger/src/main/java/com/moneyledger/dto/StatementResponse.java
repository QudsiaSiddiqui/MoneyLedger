package com.moneyledger.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.moneyledger.enums.EntryType;

public record StatementResponse( Long transactionId,

        EntryType type,

        BigDecimal amount,

        LocalDateTime createdAt) {

}
