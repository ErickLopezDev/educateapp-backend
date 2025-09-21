package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.CourseDto;
import com.lopezcampos.model.Course;
import com.lopezcampos.repository.CourseRepository;

@Service
public class CourseServiceImpl 
        extends AbstractCrudService<Course, Long, CourseDto, CourseRepository>{

    public CourseServiceImpl(CourseRepository repository) {
        super(repository, Course.class, CourseDto.class);
    }
}
