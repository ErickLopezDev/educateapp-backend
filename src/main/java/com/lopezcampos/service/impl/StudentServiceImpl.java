package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.StudentDto;
import com.lopezcampos.model.Student;
import com.lopezcampos.repository.StudentRepository;

@Service
public class StudentServiceImpl
        extends AbstractCrudService<Student, Long, StudentDto, StudentRepository>{

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository, Student.class, StudentDto.class);
    }

    @Override
    public StudentDto create(StudentDto dto) {
        if (dto.getDni() == null || dto.getDni().trim().isEmpty()) {
            throw new RuntimeException("DNI is required");
        }
        return super.create(dto);
    }
}
