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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.lopezcampos.dto.request.MatriculationRequestDto;
import com.lopezcampos.dto.response.MatriculationResponseDto;
import com.lopezcampos.service.impl.MatriculationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matriculations")
@Tag(name = "Matriculations")
@RequiredArgsConstructor
public class MatriculationController {

    private final MatriculationServiceImpl matriculationService;

    @GetMapping
    @Operation(summary = "Get all matriculations")
    public ResponseEntity<CollectionModel<EntityModel<MatriculationResponseDto>>> getAll() {
        List<MatriculationResponseDto> list = matriculationService.getAll();

        List<EntityModel<MatriculationResponseDto>> items = list.stream()
            .map(dto -> EntityModel.of(
                dto,
                linkTo(methodOn(MatriculationController.class).getById(dto.getIdMatriculation())).withSelfRel()
            ))
            .toList();

        CollectionModel<EntityModel<MatriculationResponseDto>> collection = CollectionModel.of(
            items,
            linkTo(methodOn(MatriculationController.class).getAll()).withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get matriculation by ID")
    public ResponseEntity<EntityModel<MatriculationResponseDto>> getById(@PathVariable Long id) {
        MatriculationResponseDto dto = matriculationService.getById(id);

        EntityModel<MatriculationResponseDto> model = EntityModel.of(
            dto,
            linkTo(methodOn(MatriculationController.class).getById(id)).withSelfRel(),
            linkTo(methodOn(MatriculationController.class).getAll()).withRel("all")
        );

        return ResponseEntity.ok(model);
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
