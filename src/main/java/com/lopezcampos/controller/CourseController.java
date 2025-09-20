package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.CourseDto;
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

    private CourseService courseService;
    private TeacherService teacherService;

    @GetMapping
    @Operation(summary = "Get all courses", description = "Retrieve a list of all courses")
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<Course> courses = courseService.getAll();
        List<CourseDto> courseDtos = ModelMapperConfig.mapList(courses, CourseDto.class);
        return ResponseEntity.ok(courseDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieve a specific course by its ID")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        Optional<Course> courseOpt = courseService.getById(id);
        if (courseOpt.isPresent()) {
            CourseDto courseDto = ModelMapperConfig.map(courseOpt.get(), CourseDto.class);
            return ResponseEntity.ok(courseDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create a new course", description = "Create a new course with the provided information")
    public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody CourseDto courseDto) {
        // Verify teacher exists
        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());
        
        Course savedCourse = courseService.create(course);
        CourseDto savedCourseDto = ModelMapperConfig.map(savedCourse, CourseDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update an existing course with the provided information")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        Optional<Course> existingCourseOpt = courseService.getById(id);
        if (existingCourseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Verify teacher exists
        Optional<Teacher> teacherOpt = teacherService.getById(courseDto.getTeacherId());
        if (teacherOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Course course = ModelMapperConfig.map(courseDto, Course.class);
        course.setTeacher(teacherOpt.get());
        
        Course updatedCourse = courseService.update(id, course);
        CourseDto updatedCourseDto = ModelMapperConfig.map(updatedCourse, CourseDto.class);
        return ResponseEntity.ok(updatedCourseDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Delete a course by its ID")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        Optional<Course> existingCourseOpt = courseService.getById(id);
        if (existingCourseOpt.isPresent()) {
            courseService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}