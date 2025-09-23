package com.lopezcampos.dto.teacher;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherPatchReqDto {
    
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @Size(max = 100, message = "Surname must not exceed 100 characters")
    private String surname;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;
}