package com.moneyledger.service;

public interface RedisIdempotencyService {

    boolean exists(String key);

    void markSuccess(
            String key,
            String value
    );

    String get(
            String key
    );
}