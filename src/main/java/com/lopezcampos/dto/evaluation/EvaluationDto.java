package com.lopezcampos.dto.evaluation;

import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDto {
    
    private Long idEvaluation;
    
    @Size(max = 50, message = "Type evaluation must not exceed 50 characters")
    private String typeEvaluation;
    
    private LocalDate date;
    
    @DecimalMin(value = "0.0", message = "Grade must be at least 0.0")
    @DecimalMax(value = "20.0", message = "Grade must not exceed 20.0")
    private float grade;
    
    @NotNull(message = "Matriculation ID is required")
    private Long matriculationId;
    
}