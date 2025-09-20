package com.lopezcampos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "matriculations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matriculation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_matriculation")
    private Long idMatriculation;
    
    @Column(name = "academic_period", length = 20)
    private String academicPeriod;
    
    @Column(name = "matriculation_date")
    private LocalDate matriculationDate;
    
    @Column(name = "matriculation_status", length = 20)
    private String matriculationStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_student", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course", nullable = false)
    private Course course;
    
    @OneToMany(mappedBy = "matriculation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Evaluation> evaluations;
    
}