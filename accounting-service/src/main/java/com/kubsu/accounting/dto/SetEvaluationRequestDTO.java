package com.kubsu.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetEvaluationRequestDTO {

    private Long studentId;

    private Long courseId;

    private Long typeOfWorkId;

    private OffsetDateTime evaluationDate;

    private Long evaluationGradeSystemId;

    private Double pointNumber;
}
