package com.lopezcampos.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MatriculationRequestDto {
    @Size(max = 20, message = "Academic period must not exceed 20 characters")
    private String academicPeriod;

    private LocalDate matriculationDate;

    @Size(max = 20, message = "Matriculation status must not exceed 20 characters")
    private String matriculationStatus;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
