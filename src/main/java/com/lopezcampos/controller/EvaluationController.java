package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.EvaluationDto;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.service.interface_.EvaluationService;
import com.lopezcampos.service.interface_.MatriculationService;
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
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations", description = "Evaluation management APIs")
@RequiredArgsConstructor
public class EvaluationController {

    private EvaluationService evaluationService;
    private MatriculationService matriculationService;

    @GetMapping
    @Operation(summary = "Get all evaluations", description = "Retrieve a list of all evaluations")
    public ResponseEntity<List<EvaluationDto>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAll();
        List<EvaluationDto> evaluationDtos = ModelMapperConfig.mapList(evaluations, EvaluationDto.class);
        return ResponseEntity.ok(evaluationDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID", description = "Retrieve a specific evaluation by its ID")
    public ResponseEntity<EvaluationDto> getEvaluationById(@PathVariable Long id) {
        Optional<Evaluation> evaluationOpt = evaluationService.getById(id);
        if (evaluationOpt.isPresent()) {
            EvaluationDto evaluationDto = ModelMapperConfig.map(evaluationOpt.get(), EvaluationDto.class);
            return ResponseEntity.ok(evaluationDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new evaluation", description = "Create a new evaluation with the provided information")
    public ResponseEntity<EvaluationDto> createEvaluation(@Valid @RequestBody EvaluationDto evaluationDto) {
        // Verify matriculation exists
        Optional<Matriculation> matriculationOpt = matriculationService.getById(evaluationDto.getMatriculationId());
        if (matriculationOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = ModelMapperConfig.map(evaluationDto, Evaluation.class);
        evaluation.setMatriculation(matriculationOpt.get());
        
        Evaluation savedEvaluation = evaluationService.create(evaluation);
        EvaluationDto savedEvaluationDto = ModelMapperConfig.map(savedEvaluation, EvaluationDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvaluationDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update evaluation", description = "Update an existing evaluation with the provided information")
    public ResponseEntity<EvaluationDto> updateEvaluation(@PathVariable Long id, @Valid @RequestBody EvaluationDto evaluationDto) {
        Optional<Evaluation> existingEvaluationOpt = evaluationService.getById(id);
        if (existingEvaluationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verify matriculation exists
        Optional<Matriculation> matriculationOpt = matriculationService.getById(evaluationDto.getMatriculationId());
        if (matriculationOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Evaluation evaluation = ModelMapperConfig.map(evaluationDto, Evaluation.class);
        evaluation.setMatriculation(matriculationOpt.get());
        
        Evaluation updatedEvaluation = evaluationService.update(id, evaluation);
        EvaluationDto updatedEvaluationDto = ModelMapperConfig.map(updatedEvaluation, EvaluationDto.class);
        return ResponseEntity.ok(updatedEvaluationDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation", description = "Delete an evaluation by its ID")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        Optional<Evaluation> existingEvaluationOpt = evaluationService.getById(id);
        if (existingEvaluationOpt.isPresent()) {
            evaluationService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}