package com.kubsu.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetAbsenceRequestDTO {

    private Long studentId;

    private Long courseId;

    private OffsetDateTime absenceDate;

    private Long absenceTypeId;
}
