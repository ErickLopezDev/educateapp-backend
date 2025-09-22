package com.lopezcampos.controller;

import java.util.List;

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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.lopezcampos.dto.request.ScheduleRequestDto;
import com.lopezcampos.dto.response.ScheduleResponseDto;
import com.lopezcampos.service.impl.ScheduleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @GetMapping
    @Operation(summary = "Get all schedules")
    public ResponseEntity<CollectionModel<EntityModel<ScheduleResponseDto>>> getAll() {
        List<ScheduleResponseDto> list = scheduleService.getAll();

        List<EntityModel<ScheduleResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(ScheduleController.class).getById(dto.getIdSchedule())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<ScheduleResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(ScheduleController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID")
    public ResponseEntity<EntityModel<ScheduleResponseDto>> getById(@PathVariable Long id) {
        ScheduleResponseDto dto = scheduleService.getById(id);

        EntityModel<ScheduleResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(ScheduleController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(ScheduleController.class).getAll()).withRel("all")
        );

        return ResponseEntity.ok(model);
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
