package com.kubsu.accounting.dto;

import com.kubsu.accounting.model.Evaluation;
import com.kubsu.accounting.model.EvaluationGrade;
import com.kubsu.accounting.model.Student;
import com.kubsu.accounting.model.WorkDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetEvaluationResponseDTO {

    private Student student;

    private OffsetDateTime evaluationDate;

    private EvaluationGrade evaluationGrade;

    private WorkDate workDate;

    public GetEvaluationResponseDTO(Evaluation evaluation) {
        student = evaluation.getStudent();
        evaluationDate = evaluation.getEvaluationDate();
        evaluationGrade = evaluation.getEvaluationGrade();
        workDate = evaluation.getWorkDate();
    }
}
