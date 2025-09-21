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

import com.lopezcampos.dto.MatriculationDto;
import com.lopezcampos.service.impl.MatriculationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matriculations")
@Tag(name = "Matriculations", description = "Matriculation management APIs")
@RequiredArgsConstructor
public class MatriculationController {

    private final MatriculationServiceImpl matriculationService;

    @GetMapping
    @Operation(summary = "Get all matriculations", description = "Retrieve a list of all matriculations")
    public ResponseEntity<List<MatriculationDto>> getAll() {
        return ResponseEntity.ok(matriculationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get matriculation by ID", description = "Retrieve a specific matriculation by its ID")
    public ResponseEntity<MatriculationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(matriculationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new matriculation", description = "Create a new matriculation with the provided information")
    public ResponseEntity<MatriculationDto> create(@Valid @RequestBody MatriculationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculationService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update matriculation", description = "Update an existing matriculation with the provided information")
    public ResponseEntity<MatriculationDto> update(@PathVariable Long id, @Valid @RequestBody MatriculationDto dto) {
        return ResponseEntity.ok(matriculationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete matriculation", description = "Delete a matriculation by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matriculationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
