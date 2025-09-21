package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.request.TeacherRequestDto;
import com.lopezcampos.dto.response.TeacherResponseDto;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.repository.TeacherRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class TeacherServiceImpl
        extends AbstractCrudService<Teacher, Long, TeacherRequestDto, TeacherResponseDto, TeacherRepository>{

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        super(teacherRepository, Teacher.class, TeacherResponseDto.class);
    }
}
