package com.moneyledger.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl
        implements KafkaProducerService {

    private final
    KafkaTemplate<String, String>
            kafkaTemplate;

    @Override
    public void publish(
            String topic,
            String message
    ) {

        kafkaTemplate.send(
                topic,
                message
        );

        System.out.println(
                "Published to Kafka : "
                        + message
        );
    }
}