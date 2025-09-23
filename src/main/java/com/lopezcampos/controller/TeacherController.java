package com.lopezcampos.controller;

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

import com.lopezcampos.controller.interface_.HateoasHelper;
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
        return ResponseEntity.ok(
                HateoasHelper.toCollectionModel(teacherService.getAll(),
                        TeacherResponseDto::getIdTeacher,
                        TeacherController.class,
                        "self",       
                        "create",     
                        "update",     
                        "delete"  
                        )
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public ResponseEntity<EntityModel<TeacherResponseDto>> getById(@PathVariable Long id) {
        TeacherResponseDto teacher = teacherService.getById(id);
        return ResponseEntity.ok(
                HateoasHelper.toModel(teacher,
                        TeacherResponseDto::getIdTeacher,
                        TeacherController.class,
                        "self",       
                        "create",     
                        "update",     
                        "delete"  
                        )
        );
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
