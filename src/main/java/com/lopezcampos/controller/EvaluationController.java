package com.lopezcampos.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lopezcampos.dto.request.EvaluationRequestDto;
import com.lopezcampos.dto.response.EvaluationResponseDto;
import com.lopezcampos.service.impl.EvaluationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationServiceImpl evaluationService;

    @GetMapping
    @Operation(summary = "Get all evaluations")
    public ResponseEntity<List<EvaluationResponseDto>> getAll() {
        return ResponseEntity.ok(evaluationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID")
    public ResponseEntity<EvaluationResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new evaluation")
    public ResponseEntity<EvaluationResponseDto> create(@Valid @RequestBody EvaluationRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.create(requestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update evaluation")
    public ResponseEntity<EvaluationResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationRequestDto requestDto
    ) {
        return ResponseEntity.ok(evaluationService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
