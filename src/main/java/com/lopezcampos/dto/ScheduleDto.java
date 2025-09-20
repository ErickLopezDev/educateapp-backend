package com.lopezcampos.dto;

import lombok.*;
import java.time.LocalTime;
import jakarta.validation.constraints.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    
    private Long idSchedule;
    
    @NotBlank(message = "Day of week is required")
    @Size(max = 20, message = "Day of week must not exceed 20 characters")
    private String dayOfWeek;
    
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    
    @Size(max = 50, message = "Classroom must not exceed 50 characters")
    private String classroom;
    
    @NotNull(message = "Course ID is required")
    private Long courseId;
    
    // Additional fields for display purposes
    private String courseName;
    private String courseCode;
}