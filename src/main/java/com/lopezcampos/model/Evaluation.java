package com.lopezcampos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "evaluations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluation")
    private Long idEvaluation;
    
    @Column(name = "type_evaluation", length = 50)
    private String typeEvaluation;
    
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "grade", precision = 5, scale = 2)
    private BigDecimal grade;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_matriculation", nullable = false)
    private Matriculation matriculation;
    
}