package com.lopezcampos.controller;

import com.lopezcampos.controller.interface_.HateoasHelper;
import com.lopezcampos.dto.request.CourseRequestDto;
import com.lopezcampos.dto.response.CourseResponseDto;
import com.lopezcampos.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping
    @Operation(summary = "Get all courses")
    public ResponseEntity<CollectionModel<EntityModel<CourseResponseDto>>> getAll() {
        return ResponseEntity.ok(
                HateoasHelper.toCollectionModel(courseService.getAll(),
                        CourseResponseDto::getIdCourse,
                        CourseController.class)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<EntityModel<CourseResponseDto>> getById(@PathVariable Long id) {
        CourseResponseDto course = courseService.getById(id);
        return ResponseEntity.ok(
                HateoasHelper.toModel(course,
                        CourseResponseDto::getIdCourse,
                        CourseController.class)
        );
    }

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseResponseDto> create(@Valid @RequestBody CourseRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing course")
    public ResponseEntity<CourseResponseDto> update(@PathVariable Long id, @Valid @RequestBody CourseRequestDto dto) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
