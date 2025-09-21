package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.request.CourseRequestDto;
import com.lopezcampos.dto.response.CourseResponseDto;
import com.lopezcampos.model.Course;
import com.lopezcampos.repository.CourseRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class CourseServiceImpl 
        extends AbstractCrudService<Course, Long, CourseRequestDto, CourseResponseDto, CourseRepository>{

    public CourseServiceImpl(CourseRepository repository) {
        super(repository, Course.class, CourseResponseDto.class);
    }
}
