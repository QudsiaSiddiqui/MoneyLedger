package com.moneyledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneyledger.entity.OutboxEvent;

public interface
OutboxEventRepository
extends JpaRepository<
        OutboxEvent,
        Long> {

    List<OutboxEvent>
    findByProcessedFalse();

}