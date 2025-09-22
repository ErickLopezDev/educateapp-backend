package com.lopezcampos.controller;

import com.lopezcampos.dto.request.CourseRequestDto;
import com.lopezcampos.dto.response.CourseResponseDto;
import com.lopezcampos.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<CourseResponseDto>>> getAll() {
        List<CourseResponseDto> list = courseService.getAll();

        List<EntityModel<CourseResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(CourseController.class).getById(dto.getIdCourse())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<CourseResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(CourseController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<EntityModel<CourseResponseDto>> getById(@PathVariable Long id) {
        CourseResponseDto dto = courseService.getById(id);

        EntityModel<CourseResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(CourseController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(CourseController.class).getAll()).withRel("all")
        );
        return ResponseEntity.ok(model);
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
