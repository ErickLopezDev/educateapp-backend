package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.request.ScheduleRequestDto;
import com.lopezcampos.dto.response.ScheduleResponseDto;
import com.lopezcampos.model.Schedule;
import com.lopezcampos.repository.ScheduleRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class ScheduleServiceImpl
        extends AbstractCrudService<Schedule, Long, ScheduleRequestDto, ScheduleResponseDto, ScheduleRepository> {

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        super(scheduleRepository, Schedule.class, ScheduleResponseDto.class);
    }
}
