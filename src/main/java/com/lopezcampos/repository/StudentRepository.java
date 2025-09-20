package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
  
}
