package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.request.ScheduleRequestDto;
import com.lopezcampos.dto.response.ScheduleResponseDto;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Schedule;
import com.lopezcampos.repository.CourseRepository;
import com.lopezcampos.repository.ScheduleRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class ScheduleServiceImpl
        extends AbstractCrudService<Schedule, Long, ScheduleRequestDto, ScheduleResponseDto, ScheduleRepository> {

    private final CourseRepository courseRepository;

    public ScheduleServiceImpl(ScheduleRepository repository, CourseRepository courseRepository) {
        super(repository, Schedule.class, ScheduleResponseDto.class);
        this.courseRepository = courseRepository;
    }

    @Override
    public ScheduleResponseDto create(ScheduleRequestDto requestDto) {
        Schedule schedule = ModelMapperConfig.map(requestDto, Schedule.class);

        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id " + requestDto.getCourseId()));
        schedule.setCourse(course);

        Schedule saved = repository.save(schedule);
        return ModelMapperConfig.map(saved, ScheduleResponseDto.class);
    }
}
