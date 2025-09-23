package com.lopezcampos.dto.teacher;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherPostReqDto {
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;
    
    @NotBlank(message = "Surname is required")
    @Size(max = 100, message = "Surname must not exceed 100 characters")
    private String surname;
    
    @NotBlank(message = "DNI is required")
    @Size(max = 20, message = "DNI must not exceed 20 characters")
    private String dni;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    @Size(max = 100, message = "Specialty must not exceed 100 characters")
    private String specialty;
    
    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;
}