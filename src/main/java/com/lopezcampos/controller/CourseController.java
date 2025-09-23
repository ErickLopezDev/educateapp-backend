package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.courses.CourseDto;
import com.lopezcampos.dto.courses.CoursePostReqDto;
import com.lopezcampos.dto.courses.CoursePutReqDto;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.service.interface_.CourseService;
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
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Course management APIs")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<CourseDto>>>> getAllCourses() {
        List<Course> courses = courseService.getAll();
        List<CourseDto> courseDtos = ModelMapperConfig.mapList(courses, CourseDto.class);

        ApiSuccessResponse<List<CourseDto>> response = ApiSuccessResponse.<List<CourseDto>>builder()
                .message("Courses retrieved successfully")
                .data(courseDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<CourseDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(CourseController.class).getAllCourses()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.getById(id);
        if (courseOpt.isPresent()) {
            CourseDto courseDto = ModelMapperConfig.map(courseOpt.get(), CourseDto.class);

            ApiSuccessResponse<CourseDto> response = ApiSuccessResponse.<CourseDto>builder()
                    .message("Course found successfully")
                    .data(courseDto)
                    .build();

            EntityModel<ApiSuccessResponse<CourseDto>> model = wrapSuccessResponse(
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
                .message("Course not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course with the provided information")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CoursePostReqDto courseDto) {
        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), teachersLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());

        Course savedCourse = courseService.create(course);
        if (savedCourse == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create course")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        CourseDto responseDto = ModelMapperConfig.map(savedCourse, CourseDto.class);

        ApiSuccessResponse<CourseDto> response = ApiSuccessResponse.<CourseDto>builder()
                .message("Course created successfully")
                .data(responseDto)
                .build();

        Long courseId = responseDto.getIdCourse();
        EntityModel<ApiSuccessResponse<CourseDto>> model = wrapSuccessResponse(
                response,
                selfLink(courseId),
                collectionLink(),
                updateLink(courseId),
                deleteLink(courseId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update course", description = "Update an existing course with the provided information")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @Valid @RequestBody CoursePutReqDto courseDto) {
        Optional<Course> existingCourseOpt = courseService.getById(id);
        if (existingCourseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Course not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), teachersLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());

        Course updatedCourse = courseService.update(id, course);

        if (updatedCourse == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to update course")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        CourseDto responseDto = ModelMapperConfig.map(updatedCourse, CourseDto.class);

        ApiSuccessResponse<CourseDto> response = ApiSuccessResponse.<CourseDto>builder()
                .message("Course updated successfully")
                .data(responseDto)
                .build();

        EntityModel<ApiSuccessResponse<CourseDto>> model = wrapSuccessResponse(
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
    @Operation(summary = "Delete course", description = "Delete a course by its ID")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        Optional<Course> existingCourseOpt = courseService.getById(id);
        if (existingCourseOpt.isPresent()) {
            courseService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Course with ID " + id + " has been deleted")
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
                .message("Course not found")
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
        return linkTo(methodOn(CourseController.class).getAllCourses()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(CourseController.class).createCourse(null)).withRel("create").withType("POST");
    }

    private Link teachersLink() {
        return linkTo(methodOn(TeacherController.class).getAllTeachers()).withRel("teachers").withType("GET");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(CourseController.class).getCourseById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(CourseController.class).updateCourse(id, null)).withRel("update").withType("PUT");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(CourseController.class).deleteCourse(id)).withRel("delete").withType("DELETE");
    }
}
