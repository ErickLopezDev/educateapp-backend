package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.MatriculationDto;
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

    private MatriculationService matriculationService;
    private StudentService studentService;
    private CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all matriculations", description = "Retrieve a list of all matriculations")
    public ResponseEntity<List<MatriculationDto>> getAllMatriculations() {
        List<Matriculation> matriculations = matriculationService.getAll();
        List<MatriculationDto> matriculationDtos = ModelMapperConfig.mapList(matriculations, MatriculationDto.class);
        return ResponseEntity.ok(matriculationDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get matriculation by ID", description = "Retrieve a specific matriculation by its ID")
    public ResponseEntity<MatriculationDto> getMatriculationById(@PathVariable Long id) {
        Optional<Matriculation> matriculationOpt = matriculationService.getById(id);
        if (matriculationOpt.isPresent()) {
            MatriculationDto matriculationDto = ModelMapperConfig.map(matriculationOpt.get(), MatriculationDto.class);
            return ResponseEntity.ok(matriculationDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new matriculation", description = "Create a new matriculation with the provided information")
    public ResponseEntity<MatriculationDto> createMatriculation(@Valid @RequestBody MatriculationDto matriculationDto) {
        // Verify student exists
        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());
        
        Matriculation savedMatriculation = matriculationService.create(matriculation);
        MatriculationDto savedMatriculationDto = ModelMapperConfig.map(savedMatriculation, MatriculationDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMatriculationDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update matriculation", description = "Update an existing matriculation with the provided information")
    public ResponseEntity<MatriculationDto> updateMatriculation(@PathVariable Long id, @Valid @RequestBody MatriculationDto matriculationDto) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verify student exists
        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Verify course exists
        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());
        
        Matriculation updatedMatriculation = matriculationService.update(id, matriculation);
        MatriculationDto updatedMatriculationDto = ModelMapperConfig.map(updatedMatriculation, MatriculationDto.class);
        return ResponseEntity.ok(updatedMatriculationDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete matriculation", description = "Delete a matriculation by its ID")
    public ResponseEntity<Void> deleteMatriculation(@PathVariable Long id) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isPresent()) {
            matriculationService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}