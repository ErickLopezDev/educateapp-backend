package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.evaluation.EvaluationDto;
import com.lopezcampos.dto.evaluation.EvaluationReqPostDto;
import com.lopezcampos.dto.evaluation.EvaluationReqPutDto;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.service.interface_.EvaluationService;
import com.lopezcampos.service.interface_.MatriculationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/evaluations")
@Tag(name = "Evaluations", description = "Evaluation management APIs")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final MatriculationService matriculationService;

    @GetMapping
    @Operation(summary = "Get all evaluations", description = "Retrieve a list of all evaluations")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<EvaluationDto>>>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAll();
        List<EvaluationDto> evaluationDtos = ModelMapperConfig.mapList(evaluations, EvaluationDto.class);

        ApiSuccessResponse<List<EvaluationDto>> response = ApiSuccessResponse.<List<EvaluationDto>>builder()
                .message("Evaluations retrieved successfully")
                .data(evaluationDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<EvaluationDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(EvaluationController.class).getAllEvaluations()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get evaluation by ID", description = "Retrieve a specific evaluation by its ID")
    public ResponseEntity<?> getEvaluationById(@PathVariable Long id) {
        Optional<Evaluation> evaluationOpt = evaluationService.getById(id);
        if (evaluationOpt.isPresent()) {
            EvaluationDto evaluationDto = ModelMapperConfig.map(evaluationOpt.get(), EvaluationDto.class);

            ApiSuccessResponse<EvaluationDto> response = ApiSuccessResponse.<EvaluationDto>builder()
                    .message("Evaluation found successfully")
                    .data(evaluationDto)
                    .build();

            EntityModel<ApiSuccessResponse<EvaluationDto>> model = wrapSuccessResponse(
                    response,
                    selfLink(id),
                    collectionLink(),
                    createLink(),
                    updateLink(id),
                    deleteLink(id)
            );

            return ResponseEntity.ok(model);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Evaluation not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new evaluation", description = "Create a new evaluation with the provided information")
    public ResponseEntity<?> createEvaluation(@Valid @RequestBody EvaluationReqPostDto evaluationDto) {
        Optional<Matriculation> matriculationOpt = matriculationService.getById(evaluationDto.getMatriculationId());
        if (matriculationOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Matriculation not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, matriculationsLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Evaluation evaluation = ModelMapperConfig.map(evaluationDto, Evaluation.class);
        evaluation.setMatriculation(matriculationOpt.get());

        Evaluation savedEvaluation = evaluationService.create(evaluation);

        if (savedEvaluation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create evaluation")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        EvaluationDto savedEvaluationDto = ModelMapperConfig.map(savedEvaluation, EvaluationDto.class);
        ApiSuccessResponse<EvaluationDto> response = ApiSuccessResponse.<EvaluationDto>builder()
                .message("Evaluation created successfully")
                .data(savedEvaluationDto)
                .build();

        Long evaluationId = savedEvaluationDto.getIdEvaluation();
        EntityModel<ApiSuccessResponse<EvaluationDto>> model = wrapSuccessResponse(
                response,
                selfLink(evaluationId),
                collectionLink(),
                updateLink(evaluationId),
                deleteLink(evaluationId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update evaluation", description = "Update an existing evaluation with the provided information")
    public ResponseEntity<?> updateEvaluation(@PathVariable Long id, @Valid @RequestBody EvaluationReqPutDto evaluationDto) {
        Optional<Evaluation> existingEvaluationOpt = evaluationService.getById(id);
        if (existingEvaluationOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Evaluation not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Matriculation> matriculationOpt = matriculationService.getById(evaluationDto.getMatriculationId());
        if (matriculationOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Matriculation not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, matriculationsLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Evaluation evaluation = ModelMapperConfig.map(evaluationDto, Evaluation.class);
        evaluation.setMatriculation(matriculationOpt.get());

        Evaluation updatedEvaluation = evaluationService.update(id, evaluation);
        if (updatedEvaluation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to update evaluation")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        EvaluationDto updatedEvaluationDto = ModelMapperConfig.map(updatedEvaluation, EvaluationDto.class);
        ApiSuccessResponse<EvaluationDto> response = ApiSuccessResponse.<EvaluationDto>builder()
                .message("Evaluation updated successfully")
                .data(updatedEvaluationDto)
                .build();

        EntityModel<ApiSuccessResponse<EvaluationDto>> model = wrapSuccessResponse(
                response,
                selfLink(id),
                collectionLink(),
                createLink(),
                updateLink(id),
                deleteLink(id)
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete evaluation", description = "Delete an evaluation by its ID")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        Optional<Evaluation> existingEvaluationOpt = evaluationService.getById(id);
        if (existingEvaluationOpt.isPresent()) {
            evaluationService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Evaluation deleted successfully")
                    .data(true)
                    .build();

            EntityModel<ApiSuccessResponse<Boolean>> model = wrapSuccessResponse(
                    response,
                    collectionLink(),
                    createLink()
            );

            return ResponseEntity.ok(model);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Evaluation not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    private <T> EntityModel<ApiSuccessResponse<T>> wrapSuccessResponse(ApiSuccessResponse<T> response, Link... links) {
        EntityModel<ApiSuccessResponse<T>> model = EntityModel.of(response);
        model.add(links);
        return model;
    }

    private EntityModel<ApiErrorResponse> wrapErrorResponse(ApiErrorResponse response, Link... links) {
        EntityModel<ApiErrorResponse> model = EntityModel.of(response);
        model.add(links);
        return model;
    }

    private Link collectionLink() {
        return linkTo(methodOn(EvaluationController.class).getAllEvaluations()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(EvaluationController.class).createEvaluation(null)).withRel("create").withType("POST");
    }

    private Link matriculationsLink() {
        return linkTo(methodOn(MatriculationController.class).getAllMatriculations()).withRel("matriculations").withType("GET");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(EvaluationController.class).getEvaluationById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(EvaluationController.class).updateEvaluation(id, null)).withRel("update").withType("PUT");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(EvaluationController.class).deleteEvaluation(id)).withRel("delete").withType("DELETE");
    }
}
