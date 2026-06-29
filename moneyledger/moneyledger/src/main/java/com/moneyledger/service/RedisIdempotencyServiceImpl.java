package com.moneyledger.service;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisIdempotencyServiceImpl
implements RedisIdempotencyService {

    private final
    StringRedisTemplate redis;

    private static final
    Duration TTL =
            Duration.ofHours(24);

    @Override
    public boolean exists(
            String key
    ) {

        return Boolean.TRUE.equals(

                redis.hasKey(
                        buildKey(key)
                )

        );
    }

    @Override
    public void markSuccess(
            String key,
            String value
    ) {

        redis.opsForValue()
                .set(

                        buildKey(key),

                        value,

                        TTL

                );
    }

    @Override
    public String get(
            String key
    ) {

        return redis.opsForValue()
                .get(

                        buildKey(
                                key
                        )

                );
    }

    private String buildKey(
            String key
    ) {

        return
                "idempotency:"
                        +
                        key;
    }
}