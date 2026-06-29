package com.moneyledger.service;

public interface KafkaProducerService {
    void publish(
            String topic,
            String message
    );
}