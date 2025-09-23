package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.courses.CourseDto;
import com.lopezcampos.dto.courses.CoursePostReqDto;
import com.lopezcampos.dto.courses.CoursePutReqDto;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.service.interface_.CourseService;
import com.lopezcampos.service.interface_.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Course management APIs")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses")
    public ResponseEntity<ApiSuccessResponse<List<CourseDto>>> getAllCourses() {
        List<Course> courses = courseService.getAll();
        List<CourseDto> courseDtos = ModelMapperConfig.mapList(courses, CourseDto.class);

        ApiSuccessResponse<List<CourseDto>> response = ApiSuccessResponse.<List<CourseDto>>builder()
                .message("Courses retrieved successfully")
                .data(courseDtos)
                .build();

        return ResponseEntity.ok(response);
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

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Course not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @PostMapping()
    @Operation(summary = "Create a new course", description = "Create a new course with the provided information")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CoursePostReqDto courseDto) {
        // Verify teacher exists
        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());

        Course savedCourse = courseService.create(course);
        if (savedCourse == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create course")
                    .errorType(500)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        CourseDto responseDto = ModelMapperConfig.map(savedCourse, CourseDto.class);

        ApiSuccessResponse<CourseDto> response = ApiSuccessResponse.<CourseDto>builder()
                .message("Course created successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Verify teacher exists
        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Teacher not found")
                    .errorType(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());

        Course updatedCourse = courseService.update(id, course);

        if (updatedCourse == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to update course")
                    .errorType(500)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        CourseDto responseDto = ModelMapperConfig.map(updatedCourse, CourseDto.class);

        ApiSuccessResponse<CourseDto> response = ApiSuccessResponse.<CourseDto>builder()
                .message("Course updated successfully")
                .data(responseDto)
                .build();

        return ResponseEntity.ok(response);
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

            return ResponseEntity.ok(response);
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message("Course not found")
                .errorType(404)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}