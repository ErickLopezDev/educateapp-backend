package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long>{
  
}
