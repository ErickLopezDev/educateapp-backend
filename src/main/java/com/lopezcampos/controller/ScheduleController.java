package com.lopezcampos.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lopezcampos.dto.request.ScheduleRequestDto;
import com.lopezcampos.dto.response.ScheduleResponseDto;
import com.lopezcampos.service.impl.ScheduleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;
    // como se manejaba antes Hateoas, solo getters
    @GetMapping
    @Operation(summary = "Get all schedules")
    public ResponseEntity<CollectionModel<EntityModel<ScheduleResponseDto>>> getAll() {
        List<EntityModel<ScheduleResponseDto>> schedules = scheduleService.getAll().stream()
                .map(t -> EntityModel.of(t,
                        linkTo(methodOn(ScheduleController.class).getById(t.getIdSchedule())).withSelfRel(),
                        linkTo(methodOn(ScheduleController.class).getAll()).withRel("schedules")))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(schedules,
                linkTo(methodOn(ScheduleController.class).getAll()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID")
    public ResponseEntity<EntityModel<ScheduleResponseDto>> getById(@PathVariable Long id) {
        ScheduleResponseDto item = scheduleService.getById(id);
        return ResponseEntity.ok(EntityModel.of(item,
                linkTo(methodOn(EvaluationController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(EvaluationController.class).getAll()).withRel("matriculations")));
    }

    @PostMapping
    @Operation(summary = "Create a new schedule")
    public ResponseEntity<ScheduleResponseDto> create(@Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update schedule")
    public ResponseEntity<ScheduleResponseDto> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto dto) {
        return ResponseEntity.ok(scheduleService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete schedule")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
