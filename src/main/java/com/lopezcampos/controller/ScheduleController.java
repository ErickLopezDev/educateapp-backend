package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.ScheduleDto;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Schedule;
import com.lopezcampos.service.interface_.CourseService;
import com.lopezcampos.service.interface_.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedules", description = "Schedule management APIs")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Retrieve a list of all schedules")
    public ResponseEntity<List<ScheduleDto>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAll();
        List<ScheduleDto> scheduleDtos = ModelMapperConfig.mapList(schedules, ScheduleDto.class);
        return ResponseEntity.ok(scheduleDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a specific schedule by its ID")
    public ResponseEntity<ScheduleDto> getScheduleById(@PathVariable Long id) {
        Optional<Schedule> scheduleOpt = scheduleService.getById(id);
        if (scheduleOpt.isPresent()) {
            ScheduleDto scheduleDto = ModelMapperConfig.map(scheduleOpt.get(), ScheduleDto.class);
            return ResponseEntity.ok(scheduleDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    @Operation(summary = "Create a new schedule", description = "Create a new schedule with the provided information")
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody ScheduleDto scheduleDto) {
        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(scheduleDto.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Schedule schedule = ModelMapperConfig.map(scheduleDto, Schedule.class);
        schedule.setCourse(courseOpt.get());

        Schedule savedSchedule = scheduleService.create(schedule);
        ScheduleDto savedScheduleDto = ModelMapperConfig.map(savedSchedule, ScheduleDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedScheduleDto);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update schedule", description = "Update an existing schedule with the provided information")
    public ResponseEntity<ScheduleDto> updateSchedule(@PathVariable Long id,
            @Valid @RequestBody ScheduleDto scheduleDto) {
        Optional<Schedule> existingScheduleOpt = scheduleService.getById(id);
        if (existingScheduleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(scheduleDto.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Schedule schedule = ModelMapperConfig.map(scheduleDto, Schedule.class);
        schedule.setCourse(courseOpt.get());

        Schedule updatedSchedule = scheduleService.update(id, schedule);
        ScheduleDto updatedScheduleDto = ModelMapperConfig.map(updatedSchedule, ScheduleDto.class);
        return ResponseEntity.ok(updatedScheduleDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule", description = "Delete a schedule by its ID")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        Optional<Schedule> existingScheduleOpt = scheduleService.getById(id);
        if (existingScheduleOpt.isPresent()) {
            scheduleService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}