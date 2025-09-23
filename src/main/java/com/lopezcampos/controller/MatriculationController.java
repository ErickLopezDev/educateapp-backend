package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.matriculation.MatriculationDto;
import com.lopezcampos.dto.matriculation.MatriculationPostReqDto;
import com.lopezcampos.dto.matriculation.MatriculationPutReqDto;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
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
@RequestMapping("/api/matriculations")
@Tag(name = "Matriculations", description = "Matriculation management APIs")
@RequiredArgsConstructor
public class MatriculationController {

    private final MatriculationService matriculationService;
    private final StudentService studentService;
    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all matriculations", description = "Retrieve a list of all matriculations")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<MatriculationDto>>>> getAllMatriculations() {
        List<Matriculation> matriculations = matriculationService.getAll();
        List<MatriculationDto> matriculationDtos = ModelMapperConfig.mapList(matriculations, MatriculationDto.class);

        ApiSuccessResponse<List<MatriculationDto>> response = ApiSuccessResponse.<List<MatriculationDto>>builder()
                .message("Matriculations retrieved successfully")
                .data(matriculationDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<MatriculationDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(MatriculationController.class).getAllMatriculations()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get matriculation by ID", description = "Retrieve a specific matriculation by its ID")
    public ResponseEntity<?> getMatriculationById(@PathVariable Long id) {
        Optional<Matriculation> matriculationOpt = matriculationService.getById(id);
        if (matriculationOpt.isPresent()) {
            MatriculationDto matriculationDto = ModelMapperConfig.map(matriculationOpt.get(), MatriculationDto.class);

            ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
                    .message("Matriculation found successfully")
                    .data(matriculationDto)
                    .build();

            EntityModel<ApiSuccessResponse<MatriculationDto>> model = wrapSuccessResponse(
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
                .message("Matriculation not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new matriculation", description = "Create a new matriculation with the provided information")
    public ResponseEntity<?> createMatriculation(@Valid @RequestBody MatriculationPostReqDto matriculationDto) {
        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Student not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, studentsLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Course not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, coursesLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());

        Matriculation savedMatriculation = matriculationService.create(matriculation);
        if (savedMatriculation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create matriculation")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        MatriculationDto responseDto = ModelMapperConfig.map(savedMatriculation, MatriculationDto.class);

        ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
                .message("Matriculation created successfully")
                .data(responseDto)
                .build();

        Long matriculationId = responseDto.getIdMatriculation();
        EntityModel<ApiSuccessResponse<MatriculationDto>> model = wrapSuccessResponse(
                response,
                selfLink(matriculationId),
                collectionLink(),
                updateLink(matriculationId),
                deleteLink(matriculationId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update matriculation", description = "Update an existing matriculation with the provided information")
    public ResponseEntity<?> updateMatriculation(@PathVariable Long id, @Valid @RequestBody MatriculationPutReqDto matriculationDto) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Matriculation not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Student> studentOpt = studentService.getById(matriculationDto.getStudentId());
        if (studentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Student not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, studentsLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Course> courseOpt = courseService.getById(matriculationDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Course not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, coursesLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Matriculation matriculation = ModelMapperConfig.map(matriculationDto, Matriculation.class);
        matriculation.setStudent(studentOpt.get());
        matriculation.setCourse(courseOpt.get());

        Matriculation updatedMatriculation = matriculationService.update(id, matriculation);
        if (updatedMatriculation == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to update matriculation")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        MatriculationDto responseDto = ModelMapperConfig.map(updatedMatriculation, MatriculationDto.class);

        ApiSuccessResponse<MatriculationDto> response = ApiSuccessResponse.<MatriculationDto>builder()
                .message("Matriculation updated successfully")
                .data(responseDto)
                .build();

        EntityModel<ApiSuccessResponse<MatriculationDto>> model = wrapSuccessResponse(
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
    @Operation(summary = "Delete matriculation", description = "Delete a matriculation by its ID")
    public ResponseEntity<?> deleteMatriculation(@PathVariable Long id) {
        Optional<Matriculation> existingMatriculationOpt = matriculationService.getById(id);
        if (existingMatriculationOpt.isPresent()) {
            matriculationService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Matriculation deleted successfully")
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
                .message("Matriculation not found")
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
        return linkTo(methodOn(MatriculationController.class).getAllMatriculations()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(MatriculationController.class).createMatriculation(null)).withRel("create").withType("POST");
    }

    private Link studentsLink() {
        return linkTo(methodOn(StudentController.class).getAllStudents()).withRel("students").withType("GET");
    }

    private Link coursesLink() {
        return linkTo(methodOn(CourseController.class).getAllCourses()).withRel("courses").withType("GET");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(MatriculationController.class).getMatriculationById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(MatriculationController.class).updateMatriculation(id, null)).withRel("update").withType("PUT");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(MatriculationController.class).deleteMatriculation(id)).withRel("delete").withType("DELETE");
    }
}
