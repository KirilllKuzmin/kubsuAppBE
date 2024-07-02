package com.kubsu.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "specialties")
@NoArgsConstructor
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "degree_of_study_id")
    private DegreeOfStudy degreeOfStudy;

    public Specialty(String name, Faculty faculty, DegreeOfStudy degreeOfStudy) {
        this.name = name;
        this.faculty = faculty;
        this.degreeOfStudy = degreeOfStudy;
    }
}
