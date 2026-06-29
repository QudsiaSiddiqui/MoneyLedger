package com.moneyledger.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventConsumer {

    @KafkaListener(
            topics = "transactions.completed",
            groupId = "moneyledger-group"
    )
    public void consume(
            String message
    ) {

        System.out.println(
                "Received Event : "
                        + message
        );
    }
}