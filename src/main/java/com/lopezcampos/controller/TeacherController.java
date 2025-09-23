package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.teacher.TeacherDto;
import com.lopezcampos.dto.teacher.TeacherPatchReqDto;
import com.lopezcampos.dto.teacher.TeacherPostReqDto;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.service.interface_.TeacherService;
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
@RequestMapping("/api/teachers")
@Tag(name = "Teachers", description = "Teacher management APIs")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all teachers", description = "Retrieve a list of all teachers")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<TeacherDto>>>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAll();
        List<TeacherDto> teacherDtos = ModelMapperConfig.mapList(teachers, TeacherDto.class);

        ApiSuccessResponse<List<TeacherDto>> response = ApiSuccessResponse.<List<TeacherDto>>builder()
                .message("Teachers retrieved successfully")
                .data(teacherDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<TeacherDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(TeacherController.class).getAllTeachers()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Retrieve a specific teacher by their ID")
    public ResponseEntity<?> getTeacherById(@PathVariable Long id) {
        Optional<Teacher> teacherOpt = teacherService.getById(id);
        if (teacherOpt.isPresent()) {
            TeacherDto teacherDto = ModelMapperConfig.map(teacherOpt.get(), TeacherDto.class);

            ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                    .message("Teacher found successfully")
                    .data(teacherDto)
                    .build();

            EntityModel<ApiSuccessResponse<TeacherDto>> model = wrapSuccessResponse(
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
                .message("Teacher not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new teacher", description = "Create a new teacher with the provided information")
    public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherPostReqDto teacherDto) {
        Teacher teacher = ModelMapperConfig.map(teacherDto, Teacher.class);
        Teacher savedTeacher = teacherService.create(teacher);

        if (savedTeacher == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create teacher")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        TeacherDto responseDto = ModelMapperConfig.map(savedTeacher, TeacherDto.class);
        ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                .message("Teacher created successfully")
                .data(responseDto)
                .build();

        Long teacherId = responseDto.getIdTeacher();
        EntityModel<ApiSuccessResponse<TeacherDto>> model = wrapSuccessResponse(
                response,
                selfLink(teacherId),
                collectionLink(),
                updateLink(teacherId),
                deleteLink(teacherId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update teacher", description = "Update specific fields of an existing teacher")
    public ResponseEntity<?> patchTeacher(@PathVariable Long id, @RequestBody TeacherPatchReqDto teacherDto) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Teacher existingTeacher = existingTeacherOpt.get();

        if (teacherDto.getName() != null) {
            existingTeacher.setName(teacherDto.getName());
        }
        if (teacherDto.getPhone() != null) {
            existingTeacher.setPhone(teacherDto.getPhone());
        }
        if (teacherDto.getSurname() != null) {
            existingTeacher.setSurname(teacherDto.getSurname());
        }
        if (teacherDto.getStatus() != null) {
            existingTeacher.setStatus(teacherDto.getStatus());
        }

        Teacher updatedTeacher = teacherService.update(id, existingTeacher);
        TeacherDto responseDto = ModelMapperConfig.map(updatedTeacher, TeacherDto.class);

        ApiSuccessResponse<TeacherDto> response = ApiSuccessResponse.<TeacherDto>builder()
                .message("Teacher updated successfully")
                .data(responseDto)
                .build();

        EntityModel<ApiSuccessResponse<TeacherDto>> model = wrapSuccessResponse(
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
    @Operation(summary = "Delete teacher", description = "Delete a teacher by their ID")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        Optional<Teacher> existingTeacherOpt = teacherService.getById(id);
        if (existingTeacherOpt.isPresent()) {
            teacherService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Teacher deleted successfully")
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
                .message("Teacher not found")
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
        return linkTo(methodOn(TeacherController.class).getAllTeachers()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(TeacherController.class).createTeacher(null)).withRel("create").withType("POST");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(TeacherController.class).getTeacherById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(TeacherController.class).patchTeacher(id, null)).withRel("update").withType("PATCH");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(TeacherController.class).deleteTeacher(id)).withRel("delete").withType("DELETE");
    }
}
