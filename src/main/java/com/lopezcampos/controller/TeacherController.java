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

import com.lopezcampos.dto.request.TeacherRequestDto;
import com.lopezcampos.dto.response.TeacherResponseDto;
import com.lopezcampos.service.impl.TeacherServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherServiceImpl teacherService;

    @GetMapping
    @Operation(summary = "Get all teachers")
    public ResponseEntity<CollectionModel<EntityModel<TeacherResponseDto>>> getAll() {
        List<TeacherResponseDto> list = teacherService.getAll();

        List<EntityModel<TeacherResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(TeacherController.class).getById(dto.getIdTeacher())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<TeacherResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(TeacherController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<EntityModel<TeacherResponseDto>> getById(@PathVariable Long id) {
        TeacherResponseDto dto = teacherService.getById(id);

        EntityModel<TeacherResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(TeacherController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(TeacherController.class).getAll()).withRel("all")
        );

        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Create a new teacher")
    public ResponseEntity<TeacherResponseDto> create(@Valid @RequestBody TeacherRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher")
    public ResponseEntity<TeacherResponseDto> update(@PathVariable Long id, @Valid @RequestBody TeacherRequestDto dto) {
        return ResponseEntity.ok(teacherService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
