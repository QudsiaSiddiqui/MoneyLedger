package com.moneyledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneyledger.entity.IdempotencyKey;

public interface IdempotencyRepository extends JpaRepository<IdempotencyKey, String> {

}
