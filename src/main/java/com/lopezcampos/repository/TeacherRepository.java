package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
  
}
