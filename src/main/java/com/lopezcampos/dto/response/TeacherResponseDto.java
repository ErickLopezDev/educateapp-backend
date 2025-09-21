package com.lopezcampos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TeacherResponseDto {
    private Long idTeacher;
    private String name;
    private String surname;
    private String dni;
    private String email;
    private String phone;
    private String specialty;
    private String status;
}
