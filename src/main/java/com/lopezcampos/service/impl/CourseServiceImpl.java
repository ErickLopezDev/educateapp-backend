package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Course;
import com.lopezcampos.repository.CourseRepository;
import com.lopezcampos.service.interface_.CourseService;

@Service
public class CourseServiceImpl extends AbstractCrudService<Course, Long, CourseRepository> implements CourseService {

    public CourseServiceImpl(CourseRepository courseRepository) {
        super(courseRepository);
    }
}