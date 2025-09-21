package com.lopezcampos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EvaluationResponseDto {
    private Long idEvaluation;
    private String typeEvaluation;
    private LocalDate date;
    private BigDecimal grade;

    private String studentName;
    private String studentSurname;
    private String courseName;
    private String courseCode;
}
