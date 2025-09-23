package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.teacher.TeacherDto;
import com.lopezcampos.dto.teacher.TeacherPostReqDto;
import com.lopezcampos.dto.teacher.TeacherPatchReqDto;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.response.ApiErrorResponse;
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
    public ResponseEntity<ApiSuccessResponse<List<TeacherDto>>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        List<TeacherDto> teacherDtos = ModelMapperConfig.mapList(teachers, TeacherDto.class);

        ApiSuccessResponse<List<TeacherDto>> response = ApiSuccessResponse.<List<TeacherDto>>builder()
                .message("Teachers retrieved successfully")
                .data(teacherDtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieve a specific teacher by their ID")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacherOpt = teacherService.getById(id);
        if (teacherOpt.isPresent()) {
            TeacherDto teacherDto = ModelMapperConfig.map(teacherOpt.get(), TeacherDto.class);

            ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                    .message("Teacher found successfully")
                    .data(teacherDto)
                    .build();

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Teacher not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PostMapping()
    @Operation(summary = "Create a new teacher", description = "Create a new teacher with the provided information")
    public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherPostReqDto teacherDto) {
        Teacher teacher = ModelMapperConfig.map(teacherDto, Teacher.class);
        Teacher savedTeacher = teacherService.create(teacher);

        if (savedTeacher == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create teacher")
                    .errorType(500)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        TeacherDto responseDto = ModelMapperConfig.map(savedTeacher, TeacherDto.class);
        ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                .message("Teacher created successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update teacher", description = "Update specific fields of an existing teacher")
    public ResponseEntity<?> patchTeacher(@PathVariable Long id, @RequestBody TeacherPatchReqDto teacherDto) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Teacher existingTeacher = existingTeacherOpt.get();
        
        // PATCH: solo actualiza campos que NO son null
        if (teacherDto.getName() != null) {
            existingTeacher.setName(teacherDto.getName());
        }
        if (teacherDto.getPhone() != null) {
            existingTeacher.setPhone(teacherDto.getPhone());
        }
        if (teacherDto.getSurname() != null) {
            existingTeacher.setSurname(teacherDto.getSurname());
        }
        if (teacherDto.getStatus() != null) {
            existingTeacher.setStatus(teacherDto.getStatus());
        }


        Teacher updatedTeacher = teacherService.update(id, existingTeacher);
        TeacherDto responseDto = ModelMapperConfig.map(updatedTeacher, TeacherDto.class);

        ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                .message("Teacher updated successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher", description = "Delete a teacher by their ID")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isPresent()) {
            teacherService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Teacher deleted successfully")
                    .data(true)
                    .build();

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Teacher not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}