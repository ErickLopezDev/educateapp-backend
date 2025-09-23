package com.lopezcampos.dto.courses;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {
    
    private Long idCourse;
    
    @NotBlank(message = "Name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    private String name;
    
    @NotBlank(message = "Code is required")
    @Size(max = 20, message = "Code must not exceed 20 characters")
    private String code;
    
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 10, message = "Credits must not exceed 10")
    private Integer credits;
    
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 12, message = "Semester must not exceed 12")
    private Integer semester;
    
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;
    
    private String teacherName;
    private String teacherSurname;
}