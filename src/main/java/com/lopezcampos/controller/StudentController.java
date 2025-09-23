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

import com.lopezcampos.controller.interface_.HateoasHelper;
import com.lopezcampos.dto.request.StudentRequestDto;
import com.lopezcampos.dto.response.StudentResponseDto;
import com.lopezcampos.dto.response.TeacherResponseDto;
import com.lopezcampos.service.impl.StudentServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @GetMapping
    @Operation(summary = "Get all students")
    public ResponseEntity<CollectionModel<EntityModel<StudentResponseDto>>> getAll() {
        return ResponseEntity.ok(
                HateoasHelper.toCollectionModel(studentService.getAll(),
                        StudentResponseDto::getIdStudent,
                        StudentController.class)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public ResponseEntity<EntityModel<StudentResponseDto>> getById(@PathVariable Long id) {
        StudentResponseDto student = studentService.getById(id);
        return ResponseEntity.ok(
                HateoasHelper.toModel(student,
                        StudentResponseDto::getIdStudent,
                        StudentController.class)
        );
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
