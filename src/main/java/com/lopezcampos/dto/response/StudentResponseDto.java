package com.lopezcampos.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class StudentResponseDto {
    private Long idStudent;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private LocalDate enrollmentDate;
    private String status;
}
