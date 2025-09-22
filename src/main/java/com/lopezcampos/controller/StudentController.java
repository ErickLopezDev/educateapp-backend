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


import com.lopezcampos.dto.request.StudentRequestDto;
import com.lopezcampos.dto.response.StudentResponseDto;
import com.lopezcampos.service.impl.StudentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<CollectionModel<EntityModel<StudentResponseDto>>> getAll() {
        List<StudentResponseDto> list = studentService.getAll();

        List<EntityModel<StudentResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(StudentController.class).getById(dto.getIdStudent())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<StudentResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(StudentController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<EntityModel<StudentResponseDto>> getById(@PathVariable Long id) {
        StudentResponseDto dto = studentService.getById(id);

        EntityModel<StudentResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(StudentController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(StudentController.class).getAll()).withRel("all")
        );

        return ResponseEntity.ok(model);
    }

    @PostMapping
    @Operation(summary = "Create a new student")
    public ResponseEntity<StudentResponseDto> create(@Valid @RequestBody StudentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student")
    public ResponseEntity<StudentResponseDto> update(@PathVariable Long id, @Valid @RequestBody StudentRequestDto dto) {
        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
