package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
  
}
