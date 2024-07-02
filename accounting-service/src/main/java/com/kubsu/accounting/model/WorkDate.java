package com.kubsu.accounting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "work_dates", schema = "accounting_schema")
public class WorkDate {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @Getter
    @Column(name = "work_date")
    private OffsetDateTime workDate;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_of_work_id")
    private TypeOfWork typeOfWork;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "evaluation_grade_system_id")
    private EvaluationGradeSystem evaluationGradeSystem;

    @OneToMany(mappedBy = "workDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluation> evaluations;

    public WorkDate(Timetable timetable, OffsetDateTime workDate, TypeOfWork typeOfWork, EvaluationGradeSystem evaluationGradeSystem) {
        this.timetable = timetable;
        this.workDate = workDate;
        this.typeOfWork = typeOfWork;
        this.evaluationGradeSystem = evaluationGradeSystem;
    }
}