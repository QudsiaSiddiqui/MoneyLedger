package com.moneyledger.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneyledger.dto.CreateAccountRequest;
import com.moneyledger.dto.StatementResponse;
import com.moneyledger.dto.TransferRequest;
import com.moneyledger.entity.Account;
import com.moneyledger.entity.IdempotencyKey;
import com.moneyledger.entity.LedgerEntry;
import com.moneyledger.entity.OutboxEvent;
import com.moneyledger.entity.Transaction;
import com.moneyledger.enums.EntryType;
import com.moneyledger.exceptions.InsufficientFundsException;
import com.moneyledger.repository.AccountRepository;
import com.moneyledger.repository.IdempotencyRepository;
import com.moneyledger.repository.LedgerEntryRepository;
import com.moneyledger.repository.OutboxEventRepository;
import com.moneyledger.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final IdempotencyRepository idempotencyRepo;
    private final RedisIdempotencyService redisService;
    private final OutboxEventRepository outboxRepository;

    @Override
    @Transactional
    public String transfer(TransferRequest request) {

        System.out.println("Key = " + request.idempotencyKey());

        System.out.println(
                "Redis Exists = "
                        + redisService.exists(
                                request.idempotencyKey()));
        // STEP 1 — Check duplicate FIRST
    if (request.idempotencyKey() != null
        && redisService.exists(request.idempotencyKey())) {

    return "Transaction already processed";
}
    // STEP 2 — Reserve key
    IdempotencyKey key = null;

    if (request.idempotencyKey() != null) {

        key =
                IdempotencyKey.builder()
                        .key( request.idempotencyKey())
                        .status("PENDING")
                        .createdAt(LocalDateTime.now())
                        .build();
        idempotencyRepo.save(key);
 }
    // STEP 3 — Lock + fetch accounts
        Account sender = accountRepository.findById(request.fromAccountId())
                .orElseThrow();

        Account receiver = accountRepository.findById(request.toAccountId())
                .orElseThrow();

        // STEP 4 — Validate funds
        if (sender.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient balance");
        }
        // STEP 5 — Update balances
        sender.setBalance(
                sender.getBalance().subtract(request.amount()));

        receiver.setBalance(
                receiver.getBalance().add(request.amount()));

        accountRepository.save(sender);
        accountRepository.save(receiver);
            // STEP 6 — Create transaction
        Transaction transaction = Transaction.builder()
                .fromAccountId(sender.getId())
                .toAccountId(receiver.getId())
                .amount(request.amount())
                .status("SUCCESS")
                .createdAt(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);
        OutboxEvent event =
        OutboxEvent.builder()
                .aggregateType(
                        "Transaction"
                )
                .aggregateId(
                        transaction.getId()
                )
                .eventType(
                        "TransactionCompleted"
                )
                .payload(
                        """
                        {
                          "transactionId": %d,
                          "amount": %s,
                          "fromAccountId": %d,
                          "toAccountId": %d
                        }
                        """
                        .formatted(
                                transaction.getId(),
                                transaction.getAmount(),
                                transaction.getFromAccountId(),
                                transaction.getToAccountId()
                        )
                )
                .processed(
                        false
                )
                .createdAt(
                        LocalDateTime.now()
                )
                .build();

            outboxRepository.save(
                    event
            );

               // STEP 7 — Ledger entries
        LedgerEntry debitEntry = LedgerEntry.builder()
                .transactionId(transaction.getId())
                .accountId(sender.getId())
                .amount(request.amount())
                .entryType(EntryType.DEBIT)
                .createdAt(LocalDateTime.now())
                .build();

        LedgerEntry creditEntry = LedgerEntry.builder()
                .transactionId(transaction.getId())
                .accountId(receiver.getId())
                .amount(request.amount())
                .entryType(EntryType.CREDIT)
                .createdAt(LocalDateTime.now())
                .build();

        ledgerEntryRepository.save(debitEntry);
        ledgerEntryRepository.save(creditEntry);

        // STEP 8 — Mark success
    if (key != null) {

        key.setStatus("SUCCESS");

        key.setTransactionId(
                transaction.getId()
        );

        idempotencyRepo.save(key);

        redisService.markSuccess(
                request.idempotencyKey(),
                transaction.getId().toString());
    }

        return "Transfer Successful";
    }

    @Override
    public List<StatementResponse> getStatement(Long accountId) {

        return ledgerEntryRepository
                .findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(
                        entry ->
                                new StatementResponse(

                                        entry.getTransactionId(),

                                        entry.getEntryType(),

                                        entry.getAmount(),

                                        entry.getCreatedAt()
                                )
                )
                .toList();
    }

    public Account create(CreateAccountRequest request) {
        Account account = Account.builder()
                .ownerName(request.ownerName())
                .balance(request.balance())
                .currency(request.currency())
                .accountNumber(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepository.save(account);
    }
}
