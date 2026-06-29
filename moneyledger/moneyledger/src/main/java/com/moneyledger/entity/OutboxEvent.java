package com.moneyledger.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(
            strategy =
            GenerationType.IDENTITY
    )
    private Long id;

    private String aggregateType;

    private Long aggregateId;

    private String eventType;

    @Column(
            columnDefinition = "TEXT"
    )
    private String payload;

    private Boolean processed;

    private LocalDateTime createdAt;
}