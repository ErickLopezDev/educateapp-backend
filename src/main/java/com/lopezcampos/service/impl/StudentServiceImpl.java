package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Student;
import com.lopezcampos.repository.StudentRepository;
import com.lopezcampos.service.interface_.StudentService;

@Service
public class StudentServiceImpl extends AbstractCrudService<Student, Long, StudentRepository> implements StudentService {

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository);
    }

    @Override
    public Student create(Student student) {
        if (student.getDni() == null || student.getDni().trim().isEmpty()) {
            throw new RuntimeException("DNI is required");
        }
        return super.create(student);
    }
}