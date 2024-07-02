package com.kubsu.accounting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "absences", schema = "accounting_schema")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "absence_date")
    private OffsetDateTime absenceDate;

    @Column(name = "event_date")
    private OffsetDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "absence_type_id")
    private AbsenceType absenceType;

    public Absence(Timetable timetable, Student student, OffsetDateTime absenceDate, OffsetDateTime eventDate, AbsenceType absenceType) {
        this.timetable = timetable;
        this.student = student;
        this.absenceDate = absenceDate;
        this.eventDate = eventDate;
        this.absenceType = absenceType;
    }
}
