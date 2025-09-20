package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Teacher;
import com.lopezcampos.repository.TeacherRepository;
import com.lopezcampos.service.interface_.TeacherService;

@Service
public class TeacherServiceImpl extends AbstractCrudService<Teacher, Long, TeacherRepository> implements TeacherService {

    public TeacherServiceImpl(TeacherRepository teacherRepository) {
        super(teacherRepository);
    }
}