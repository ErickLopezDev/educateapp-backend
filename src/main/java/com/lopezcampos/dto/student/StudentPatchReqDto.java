package com.lopezcampos.dto.student;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentPatchReqDto {
    
    // Para PATCH, ningún campo es obligatorio (@NotBlank/@NotNull)
    // Solo validamos el formato/tamaño si el campo está presente
    
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String name;
    
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String surname;
    
    @Size(max = 255, message = "Adress must not exceed 100 characters")
    private String address;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 20, message = "Status must not exceed 20 characters")
    private String status;

}