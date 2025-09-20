package com.lopezcampos.dto;

import lombok.*;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    
    private Long idStudent;
    
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
    
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;
    
    private LocalDate birthDate;
    
    private LocalDate enrollmentDate;
    
    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;
}