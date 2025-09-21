package com.lopezcampos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CourseResponseDto {
    private Long idCourse;
    private String name;
    private String code;
    private Integer credits;
    private Integer semester;

    private String teacherName;
    private String teacherSurname;
}
