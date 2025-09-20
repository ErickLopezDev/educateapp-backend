package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Student;
import com.lopezcampos.repository.StudentRepository;
import com.lopezcampos.service.interface_.StudentService;

@Service
public class StudentServiceImpl extends AbstractCrudService<Student, Long, StudentRepository> implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        super(studentRepository);
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        if (student.getDni() == null || student.getDni().trim().isEmpty()) {
            throw new RuntimeException("DNI is required");
        }
        return super.create(student);
    }
}