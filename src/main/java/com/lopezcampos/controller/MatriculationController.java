package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.matriculation.MatriculationDto;
import com.lopezcampos.dto.matriculation.MatriculationPostReqDto;
import com.lopezcampos.dto.matriculation.MatriculationPutReqDto;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.model.Student;
import com.lopezcampos.service.interface_.CourseService;
import com.lopezcampos.service.interface_.MatriculationService;
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
@RequestMapping("/api/matriculations")
@Tag(name = "Matriculations", description = "Matriculation management APIs")
@RequiredArgsConstructor
public class MatriculationController {

    private final MatriculationService matriculationService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all matriculations", description = "Retrieve a list of all matriculations")
    public ResponseEntity<ApiSuccessResponse<List<MatriculationDto>>> getAllMatriculations() {
        List<Matriculation> matriculations = matriculationService.getAll();
        List<MatriculationDto> matriculationDtos = ModelMapperConfig.mapList(matriculations, MatriculationDto.class);
        
        ApiSuccessResponse<List<MatriculationDto>> response = ApiSuccessResponse.<List<MatriculationDto>>builder()
            .message("Matriculations retrieved successfully")
            .data(matriculationDtos)
            .build();
            
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get matriculation by ID", description = "Retrieve a specific matriculation by its ID")
    public ResponseEntity<?> getMatriculationById(@PathVariable Long id) {
        Optional<Matriculation> matriculationOpt = matriculationService.getById(id);
        if (matriculationOpt.isPresent()) {
            MatriculationDto matriculationDto = ModelMapperConfig.map(matriculationOpt.get(), MatriculationDto.class);
            
            ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
                .message("Matriculation found successfully")
                .data(matriculationDto)
                .build();
                
            return ResponseEntity.ok(response);
        }
        
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .message("Matriculation not found")
            .errorType(404)
            .build();
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PostMapping()
    @Operation(summary = "Create a new matriculation", description = "Create a new matriculation with the provided information")
    public ResponseEntity<?> createMatriculation(@Valid @RequestBody MatriculationPostReqDto matriculationDto) {
        // Verify student exists
        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Student not found")
                .errorType(404)
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Course not found")
                .errorType(404)
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());

        Matriculation savedMatriculation = matriculationService.create(matriculation);
        if (savedMatriculation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Failed to create matriculation")
                .errorType(500)
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        
        MatriculationDto responseDto = ModelMapperConfig.map(savedMatriculation, MatriculationDto.class);
        
        ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
            .message("Matriculation created successfully")
            .data(responseDto)
            .build();
            
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update matriculation", description = "Update an existing matriculation with the provided information")
    public ResponseEntity<?> updateMatriculation(@PathVariable Long id, @Valid @RequestBody MatriculationPutReqDto matriculationDto) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Matriculation not found")
                .errorType(404)
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Verify student exists
        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Student not found")
                .errorType(404)
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Course not found")
                .errorType(404)
                .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());

        Matriculation updatedMatriculation = matriculationService.update(id, matriculation);
        if (updatedMatriculation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Failed to update matriculation")
                .errorType(500)
                .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        
        MatriculationDto responseDto = ModelMapperConfig.map(updatedMatriculation, MatriculationDto.class);
        
        ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
            .message("Matriculation updated successfully")
            .data(responseDto)
            .build();
            
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete matriculation", description = "Delete a matriculation by its ID")
    public ResponseEntity<?> deleteMatriculation(@PathVariable Long id) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isPresent()) {
            matriculationService.delete(id);
            
            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                .message("Matriculation deleted successfully")
                .data(true)
                .build();
                
            return ResponseEntity.ok(response);
        }
        
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
            .message("Matriculation not found")
            .errorType(404)
            .build();
            
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}