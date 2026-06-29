package com.moneyledger.service;

import java.util.List;

import com.moneyledger.dto.CreateAccountRequest;
import com.moneyledger.dto.StatementResponse;
import com.moneyledger.dto.TransferRequest;
import com.moneyledger.entity.Account;


public interface  TransactionService {

    String transfer(TransferRequest request);
    List<StatementResponse>getStatement(Long accountId);
    Account create(CreateAccountRequest request);
}
