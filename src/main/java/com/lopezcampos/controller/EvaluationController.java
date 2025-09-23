package com.lopezcampos.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lopezcampos.controller.interface_.HateoasHelper;
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
    public ResponseEntity<CollectionModel<EntityModel<EvaluationResponseDto>>> getAll() {
        return ResponseEntity.ok(
                HateoasHelper.toCollectionModel(evaluationService.getAll(),
                        EvaluationResponseDto::getIdEvaluation,
                        EvaluationController.class)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID")
    public ResponseEntity<EntityModel<EvaluationResponseDto>> getById(@PathVariable Long id) {
        EvaluationResponseDto evaluation = evaluationService.getById(id);
        return ResponseEntity.ok(
                HateoasHelper.toModel(evaluation,
                        EvaluationResponseDto::getIdEvaluation,
                        EvaluationController.class)
        );
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
