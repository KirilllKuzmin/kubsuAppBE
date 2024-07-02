package com.kubsu.accounting.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "number_time_classes_held", schema = "accounting_schema")
public class NumberTimeClassHeld {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time")
    private OffsetDateTime startTime;

    @Column(name = "end_time")
    private OffsetDateTime endTime;
}
