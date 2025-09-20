package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.StudentDto;
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
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<Student> students = studentService.getAll();
        List<StudentDto> studentDtos = ModelMapperConfig.mapList(students, StudentDto.class);
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOpt = studentService.getById(id);
        if (studentOpt.isPresent()) {
            StudentDto studentDto = ModelMapperConfig.map(studentOpt.get(), StudentDto.class);
            return ResponseEntity.ok(studentDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new student", description = "Create a new student with the provided information")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        Student student = ModelMapperConfig.map(studentDto, Student.class);
        Student savedStudent = studentService.create(student);
        StudentDto savedStudentDto = ModelMapperConfig.map(savedStudent, StudentDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudentDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student", description = "Update an existing student with the provided information")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDto) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isPresent()) {
            Student student = ModelMapperConfig.map(studentDto, Student.class);
            Student updatedStudent = studentService.update(id, student);
            StudentDto updatedStudentDto = ModelMapperConfig.map(updatedStudent, StudentDto.class);
            return ResponseEntity.ok(updatedStudentDto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student", description = "Delete a student by their ID")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isPresent()) {
            studentService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}