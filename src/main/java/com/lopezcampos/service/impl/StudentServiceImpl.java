package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.request.StudentRequestDto;
import com.lopezcampos.dto.response.StudentResponseDto;
import com.lopezcampos.model.Student;
import com.lopezcampos.repository.StudentRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class StudentServiceImpl
        extends AbstractCrudService<Student, Long, StudentRequestDto, StudentResponseDto, StudentRepository>{

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository, Student.class, StudentResponseDto.class);
    }

    @Override
    public StudentResponseDto create(StudentRequestDto dto) {
        if (dto.getDni() == null || dto.getDni().trim().isEmpty()) {
            throw new RuntimeException("DNI is required");
        }
        return super.create(dto);
    }
}
