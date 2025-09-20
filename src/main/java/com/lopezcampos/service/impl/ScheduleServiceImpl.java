package com.lopezcampos.service.impl;

import com.lopezcampos.model.Schedule;
import com.lopezcampos.repository.ScheduleRepository;
import com.lopezcampos.service.interface_.ScheduleService;
import org.springframework.stereotype.Service;

@Service
public class ScheduleServiceImpl extends AbstractCrudService<Schedule, Long, ScheduleRepository> implements ScheduleService {

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        super(scheduleRepository);
    }
}