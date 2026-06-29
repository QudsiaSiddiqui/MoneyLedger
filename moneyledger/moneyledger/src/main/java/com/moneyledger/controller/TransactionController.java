package com.moneyledger.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneyledger.dto.CreateAccountRequest;
import com.moneyledger.dto.StatementResponse;
import com.moneyledger.dto.TransferRequest;
import com.moneyledger.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> transfer(
            @RequestBody TransferRequest request) {

        String result =
        transactionService.transfer(request);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/accounts/{id}/statement")
        public ResponseEntity<List<StatementResponse>> getStatement(@PathVariable Long id) {
            return ResponseEntity.ok(transactionService.getStatement(id));
        }
    @PostMapping("/accounts")
        public ResponseEntity<Object> create(@RequestBody CreateAccountRequest request) {
            return ResponseEntity.ok(transactionService.create(request));
        }
}