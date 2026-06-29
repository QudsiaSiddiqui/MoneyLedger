package com.moneyledger.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(String ownerName,BigDecimal balance, String currency) {

}
