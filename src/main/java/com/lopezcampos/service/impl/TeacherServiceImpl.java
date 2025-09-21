package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.TeacherDto;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.repository.TeacherRepository;

@Service
public class TeacherServiceImpl
        extends AbstractCrudService<Teacher, Long, TeacherDto, TeacherRepository>{

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        super(teacherRepository, Teacher.class, TeacherDto.class);
    }
}
