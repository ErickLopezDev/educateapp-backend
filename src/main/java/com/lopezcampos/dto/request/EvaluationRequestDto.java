package com.lopezcampos.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluationRequestDto {
    @Size(max = 50, message = "Type evaluation must not exceed 50 characters")
    private String typeEvaluation;

    private LocalDate date;

    @DecimalMin(value = "0.0", message = "Grade must be at least 0.0")
    @DecimalMax(value = "20.0", message = "Grade must not exceed 20.0")
    private BigDecimal grade;

    @NotNull(message = "Matriculation ID is required")
    private Long matriculationId;
}
