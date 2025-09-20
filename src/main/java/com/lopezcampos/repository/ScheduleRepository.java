package com.lopezcampos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lopezcampos.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
  
}
