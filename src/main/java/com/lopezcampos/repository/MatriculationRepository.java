package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Matriculation;

public interface MatriculationRepository extends JpaRepository<Matriculation, Long> {
  
}
