package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.student.StudentDto;
import com.lopezcampos.dto.student.StudentPatchReqDto;
import com.lopezcampos.dto.student.StudentPostReqDto;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.model.Student;
import com.lopezcampos.service.interface_.StudentService;
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
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student management APIs")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    public ResponseEntity<ApiSuccessResponse<List<StudentDto>>> getAllStudents() {
        List<Student> students = studentService.getAll();
        List<StudentDto> studentDtos = ModelMapperConfig.mapList(students, StudentDto.class);

        ApiSuccessResponse<List<StudentDto>> response = ApiSuccessResponse.<List<StudentDto>>builder()
                .message("Students retrieved successfully")
                .data(studentDtos)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOpt = studentService.getById(id);
        if (studentOpt.isPresent()) {
            StudentDto studentDto = ModelMapperConfig.map(studentOpt.get(), StudentDto.class);

            ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                    .message("Student found successfully")
                    .data(studentDto)
                    .build();

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Student not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PostMapping()
    @Operation(summary = "Create a new student", description = "Create a new student with the provided information")
    public ResponseEntity<?> createStudentForm(@Valid @RequestBody StudentPostReqDto studentDto) {
        Student student = ModelMapperConfig.map(studentDto, Student.class);
        Student savedStudent = studentService.create(student);

        if (savedStudent == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create student")
                    .errorType(500)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        StudentDto responseDto = ModelMapperConfig.map(savedStudent, StudentDto.class);
        ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                .message("Student created successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "Partially update student", description = "Update specific fields of an existing student")
    public ResponseEntity<?> patchStudent(@PathVariable Long id, @Valid @RequestBody StudentPatchReqDto studentDto) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Student not found")
                    .errorType(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Student existingStudent = existingStudentOpt.get();

        // PATCH: solo actualiza campos que NO son null
        if (studentDto.getName() != null) {
            existingStudent.setName(studentDto.getName());
        }
        if (studentDto.getSurname() != null) {
            existingStudent.setSurname(studentDto.getSurname());
        }
        if (studentDto.getPhone() != null) {
            existingStudent.setPhone(studentDto.getPhone());
        }
        if (studentDto.getAddress() != null) {
            existingStudent.setAddress(studentDto.getAddress());
        }
        if (studentDto.getStatus() != null) {
            existingStudent.setStatus(studentDto.getStatus());
        }

        Student updatedStudent = studentService.update(id, existingStudent);
        StudentDto responseDto = ModelMapperConfig.map(updatedStudent, StudentDto.class);

        ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                .message("Student updated successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student by their ID")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isPresent()) {
            studentService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Student deleted successfully")
                    .data(true)
                    .build();

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Student not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

}