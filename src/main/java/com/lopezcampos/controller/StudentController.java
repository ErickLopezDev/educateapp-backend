package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.student.StudentDto;
import com.lopezcampos.dto.student.StudentPatchReqDto;
import com.lopezcampos.dto.student.StudentPostReqDto;
import com.lopezcampos.model.Student;
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
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student management APIs")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<StudentDto>>>> getAllStudents() {
        List<Student> students = studentService.getAll();
        List<StudentDto> studentDtos = ModelMapperConfig.mapList(students, StudentDto.class);

        ApiSuccessResponse<List<StudentDto>> response = ApiSuccessResponse.<List<StudentDto>>builder()
                .message("Students retrieved successfully")
                .data(studentDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<StudentDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(StudentController.class).getAllStudents()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a specific student by their ID")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        Optional<Student> studentOpt = studentService.getById(id);
        if (studentOpt.isPresent()) {
            StudentDto studentDto = ModelMapperConfig.map(studentOpt.get(), StudentDto.class);

            ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                    .message("Student found successfully")
                    .data(studentDto)
                    .build();

            EntityModel<ApiSuccessResponse<StudentDto>> model = wrapSuccessResponse(
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
                .message("Student not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new student", description = "Create a new student with the provided information")
    public ResponseEntity<?> createStudentForm(@Valid @RequestBody StudentPostReqDto studentDto) {
        Student student = ModelMapperConfig.map(studentDto, Student.class);
        Student savedStudent = studentService.create(student);

        if (savedStudent == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create student")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        StudentDto responseDto = ModelMapperConfig.map(savedStudent, StudentDto.class);
        ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                .message("Student created successfully")
                .data(responseDto)
                .build();

        Long studentId = responseDto.getIdStudent();
        EntityModel<ApiSuccessResponse<StudentDto>> model = wrapSuccessResponse(
                response,
                selfLink(studentId),
                collectionLink(),
                updateLink(studentId),
                deleteLink(studentId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PatchMapping(value = "/{id}")
    @Operation(summary = "Partially update student", description = "Update specific fields of an existing student")
    public ResponseEntity<?> patchStudent(@PathVariable Long id, @Valid @RequestBody StudentPatchReqDto studentDto) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Student not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Student existingStudent = existingStudentOpt.get();

        if (studentDto.getName() != null) {
            existingStudent.setName(studentDto.getName());
        }
        if (studentDto.getSurname() != null) {
            existingStudent.setSurname(studentDto.getSurname());
        }
        if (studentDto.getPhone() != null) {
            existingStudent.setPhone(studentDto.getPhone());
        }
        if (studentDto.getAddress() != null) {
            existingStudent.setAddress(studentDto.getAddress());
        }
        if (studentDto.getStatus() != null) {
            existingStudent.setStatus(studentDto.getStatus());
        }

        Student updatedStudent = studentService.update(id, existingStudent);
        StudentDto responseDto = ModelMapperConfig.map(updatedStudent, StudentDto.class);

        ApiSuccessResponse<StudentDto> response = ApiSuccessResponse.<StudentDto>builder()
                .message("Student updated successfully")
                .data(responseDto)
                .build();

        EntityModel<ApiSuccessResponse<StudentDto>> model = wrapSuccessResponse(
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
    @Operation(summary = "Delete student", description = "Delete a student by their ID")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Optional<Student> existingStudentOpt = studentService.getById(id);
        if (existingStudentOpt.isPresent()) {
            studentService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Student deleted successfully")
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
                .message("Student not found")
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
        return linkTo(methodOn(StudentController.class).getAllStudents()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(StudentController.class).createStudentForm(null)).withRel("create").withType("POST");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(StudentController.class).getStudentById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(StudentController.class).patchStudent(id, null)).withRel("update").withType("PATCH");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(StudentController.class).deleteStudent(id)).withRel("delete").withType("DELETE");
    }
}
