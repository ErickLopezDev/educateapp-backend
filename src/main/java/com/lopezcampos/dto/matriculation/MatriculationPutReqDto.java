package com.lopezcampos.dto.matriculation;

import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculationPutReqDto {
    
    @NotBlank(message = "Academic period is required")
    @Size(max = 20, message = "Academic period must not exceed 20 characters")
    private String academicPeriod;
    
    private LocalDate matriculationDate;
    
    @NotBlank(message = "Matriculation status is required")
    @Size(max = 20, message = "Matriculation status must not exceed 20 characters")
    private String matriculationStatus;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
}