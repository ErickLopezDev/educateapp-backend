package com.lopezcampos.dto;

import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatriculationDto {
    
    private Long idMatriculation;
    
    @Size(max = 20, message = "Academic period must not exceed 20 characters")
    private String academicPeriod;
    
    private LocalDate matriculationDate;
    
    @Size(max = 20, message = "Matriculation status must not exceed 20 characters")
    private String matriculationStatus;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    private String studentName;
    private String studentSurname;
    private String courseName;
    private String courseCode;
}