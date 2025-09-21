package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.ScheduleDto;
import com.lopezcampos.model.Schedule;
import com.lopezcampos.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl
        extends AbstractCrudService<Schedule, Long, ScheduleDto, ScheduleRepository> {

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        super(scheduleRepository, Schedule.class, ScheduleDto.class);
    }
}
