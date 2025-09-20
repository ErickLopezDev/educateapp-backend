package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.TeacherDto;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.service.interface_.TeacherService;
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
@RequestMapping("/api/teachers")
@Tag(name = "Teachers", description = "Teacher management APIs")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all teachers", description = "Retrieve a list of all teachers")
    public ResponseEntity<List<TeacherDto>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        List<TeacherDto> teacherDtos = ModelMapperConfig.mapList(teachers, TeacherDto.class);
        return ResponseEntity.ok(teacherDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieve a specific teacher by their ID")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacherOpt = teacherService.getById(id);
        if (teacherOpt.isPresent()) {
            TeacherDto teacherDto = ModelMapperConfig.map(teacherOpt.get(), TeacherDto.class);
            return ResponseEntity.ok(teacherDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new teacher", description = "Create a new teacher with the provided information")
    public ResponseEntity<TeacherDto> createTeacher(@Valid @RequestBody TeacherDto teacherDto) {
        Teacher teacher = ModelMapperConfig.map(teacherDto, Teacher.class);
        Teacher savedTeacher = teacherService.create(teacher);
        TeacherDto savedTeacherDto = ModelMapperConfig.map(savedTeacher, TeacherDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacherDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher", description = "Update an existing teacher with the provided information")
    public ResponseEntity<TeacherDto> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDto) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isPresent()) {
            Teacher teacher = ModelMapperConfig.map(teacherDto, Teacher.class);
            Teacher updatedTeacher = teacherService.update(id, teacher);
            TeacherDto updatedTeacherDto = ModelMapperConfig.map(updatedTeacher, TeacherDto.class);
            return ResponseEntity.ok(updatedTeacherDto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher", description = "Delete a teacher by their ID")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isPresent()) {
            teacherService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}