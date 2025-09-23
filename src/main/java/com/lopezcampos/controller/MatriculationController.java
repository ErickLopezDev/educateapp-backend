package com.lopezcampos.controller;

import java.util.List;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

import com.lopezcampos.controller.interface_.HateoasHelper;
import com.lopezcampos.dto.request.MatriculationRequestDto;
import com.lopezcampos.dto.response.CourseResponseDto;
import com.lopezcampos.dto.response.MatriculationResponseDto;
import com.lopezcampos.service.impl.MatriculationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/matriculations")
@Tag(name = "Matriculations")
@RequiredArgsConstructor
public class MatriculationController {

    private final MatriculationServiceImpl matriculationService;

    @GetMapping
    @Operation(summary = "Get all matriculations")
    public ResponseEntity<CollectionModel<EntityModel<MatriculationResponseDto>>> getAll() {
        return ResponseEntity.ok(
                HateoasHelper.toCollectionModel(matriculationService.getAll(),
                        MatriculationResponseDto::getIdMatriculation,
                        MatriculationController.class)
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<EntityModel<MatriculationResponseDto>> getById(@PathVariable Long id) {
        MatriculationResponseDto matriculation = matriculationService.getById(id);
        return ResponseEntity.ok(
                HateoasHelper.toModel(matriculation,
                        MatriculationResponseDto::getIdMatriculation,
                        CourseController.class)
        );
    }

    @PostMapping
    @Operation(summary = "Create a new matriculation")
    public ResponseEntity<MatriculationResponseDto> create(@Valid @RequestBody MatriculationRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matriculationService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update matriculation")
    public ResponseEntity<MatriculationResponseDto> update(@PathVariable Long id, @Valid @RequestBody MatriculationRequestDto dto) {
        return ResponseEntity.ok(matriculationService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete matriculation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matriculationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
