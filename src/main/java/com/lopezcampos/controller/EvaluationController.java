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

import com.lopezcampos.dto.EvaluationDto;
import com.lopezcampos.service.impl.EvaluationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations", description = "Evaluation management APIs")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationServiceImpl evaluationService;

    @GetMapping
    @Operation(summary = "Get all evaluations", description = "Retrieve a list of all evaluations")
    public ResponseEntity<List<EvaluationDto>> getAll() {
        return ResponseEntity.ok(evaluationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID", description = "Retrieve a specific evaluation by its ID")
    public ResponseEntity<EvaluationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new evaluation", description = "Create a new evaluation with the provided information")
    public ResponseEntity<EvaluationDto> create(@Valid @RequestBody EvaluationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update evaluation", description = "Update an existing evaluation with the provided information")
    public ResponseEntity<EvaluationDto> update(@PathVariable Long id, @Valid @RequestBody EvaluationDto dto) {
        return ResponseEntity.ok(evaluationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation", description = "Delete an evaluation by its ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
