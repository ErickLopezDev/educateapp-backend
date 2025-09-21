package com.lopezcampos.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class MatriculationResponseDto {
    private Long idMatriculation;
    private String academicPeriod;
    private LocalDate matriculationDate;
    private String matriculationStatus;

    private String studentName;
    private String studentSurname;
    private String courseName;
    private String courseCode;
}
