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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationServiceImpl evaluationService;

    @GetMapping
    @Operation(summary = "Get all evaluations")
    public ResponseEntity<CollectionModel<EntityModel<EvaluationResponseDto>>> getAll() {
        List<EvaluationResponseDto> list = evaluationService.getAll();

        List<EntityModel<EvaluationResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(EvaluationController.class).getById(dto.getIdEvaluation())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<EvaluationResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(EvaluationController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID")
    public ResponseEntity<EntityModel<EvaluationResponseDto>> getById(@PathVariable Long id) {
        EvaluationResponseDto dto = evaluationService.getById(id);

        EntityModel<EvaluationResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(EvaluationController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(EvaluationController.class).getAll()).withRel("all")
        );

        return ResponseEntity.ok(model);
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
