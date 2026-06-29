package com.moneyledger.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moneyledger.entity.OutboxEvent;
import com.moneyledger.repository.OutboxEventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final
    OutboxEventRepository
            outboxRepository;

    private final
    KafkaProducerService
            kafkaProducer;

    @Scheduled(
            fixedDelay = 5000
    )
    @Transactional
    public void publishEvents() {

        List<OutboxEvent> events =
                outboxRepository
                        .findByProcessedFalse();

        for (OutboxEvent event
                : events) {

            kafkaProducer.publish(

                    "transactions.completed",

                    event.getPayload()

            );

            event.setProcessed(
                    true
            );

            outboxRepository.save(
                    event
            );
        }
    }
}