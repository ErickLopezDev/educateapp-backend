package com.lopezcampos.controller;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.response.ApiErrorResponse;
import com.lopezcampos.dto.response.ApiSuccessResponse;
import com.lopezcampos.dto.schedule.ScheduleDto;
import com.lopezcampos.dto.schedule.SchedulePostReqDto;
import com.lopezcampos.dto.schedule.SchedulePutReqDto;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Schedule;
import com.lopezcampos.service.interface_.CourseService;
import com.lopezcampos.service.interface_.ScheduleService;
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
@RequestMapping("/api/schedules")
@Tag(name = "Schedules", description = "Schedule management APIs")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get all schedules", description = "Retrieve a list of all schedules")
    public ResponseEntity<EntityModel<ApiSuccessResponse<List<ScheduleDto>>>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAll();
        List<ScheduleDto> scheduleDtos = ModelMapperConfig.mapList(schedules, ScheduleDto.class);

        ApiSuccessResponse<List<ScheduleDto>> response = ApiSuccessResponse.<List<ScheduleDto>>builder()
                .message("Schedules retrieved successfully")
                .data(scheduleDtos)
                .build();

        EntityModel<ApiSuccessResponse<List<ScheduleDto>>> model = wrapSuccessResponse(
                response,
                linkTo(methodOn(ScheduleController.class).getAllSchedules()).withSelfRel().withType("GET"),
                createLink()
        );

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a specific schedule by its ID")
    public ResponseEntity<?> getScheduleById(@PathVariable Long id) {
        Optional<Schedule> scheduleOpt = scheduleService.getById(id);
        if (scheduleOpt.isPresent()) {
            ScheduleDto scheduleDto = ModelMapperConfig.map(scheduleOpt.get(), ScheduleDto.class);

            ApiSuccessResponse<ScheduleDto> response = ApiSuccessResponse.<ScheduleDto>builder()
                    .message("Schedule found successfully")
                    .data(scheduleDto)
                    .build();

            EntityModel<ApiSuccessResponse<ScheduleDto>> model = wrapSuccessResponse(
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
                .message("Schedule not found")
                .errorType(404)
                .build();

        EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
    }

    @PostMapping
    @Operation(summary = "Create a new schedule", description = "Create a new schedule with the provided information")
    public ResponseEntity<?> createSchedule(@Valid @RequestBody SchedulePostReqDto scheduleDto) {
        Optional<Course> courseOpt = courseService.getById(scheduleDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Course not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, coursesLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Schedule schedule = ModelMapperConfig.map(scheduleDto, Schedule.class);
        schedule.setCourse(courseOpt.get());

        Schedule savedSchedule = scheduleService.create(schedule);
        if (savedSchedule == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to create schedule")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        ScheduleDto responseDto = ModelMapperConfig.map(savedSchedule, ScheduleDto.class);

        ApiSuccessResponse<ScheduleDto> response = ApiSuccessResponse.<ScheduleDto>builder()
                .message("Schedule created successfully")
                .data(responseDto)
                .build();

        Long scheduleId = responseDto.getIdSchedule();
        EntityModel<ApiSuccessResponse<ScheduleDto>> model = wrapSuccessResponse(
                response,
                selfLink(scheduleId),
                collectionLink(),
                updateLink(scheduleId),
                deleteLink(scheduleId)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update schedule", description = "Update an existing schedule with the provided information")
    public ResponseEntity<?> updateSchedule(@PathVariable Long id, @Valid @RequestBody SchedulePutReqDto scheduleDto) {
        Optional<Schedule> existingScheduleOpt = scheduleService.getById(id);
        if (existingScheduleOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Schedule not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink(), createLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Optional<Course> courseOpt = courseService.getById(scheduleDto.getCourseId());
        if (courseOpt.isEmpty()) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Course not found")
                    .errorType(404)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, coursesLink());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorModel);
        }

        Schedule schedule = ModelMapperConfig.map(scheduleDto, Schedule.class);
        schedule.setCourse(courseOpt.get());

        Schedule updatedSchedule = scheduleService.update(id, schedule);
        if (updatedSchedule == null) {
            ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                    .message("Failed to update schedule")
                    .errorType(500)
                    .build();
            EntityModel<ApiErrorResponse> errorModel = wrapErrorResponse(errorResponse, collectionLink());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorModel);
        }

        ScheduleDto responseDto = ModelMapperConfig.map(updatedSchedule, ScheduleDto.class);

        ApiSuccessResponse<ScheduleDto> response = ApiSuccessResponse.<ScheduleDto>builder()
                .message("Schedule updated successfully")
                .data(responseDto)
                .build();

        EntityModel<ApiSuccessResponse<ScheduleDto>> model = wrapSuccessResponse(
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
    @Operation(summary = "Delete schedule", description = "Delete a schedule by its ID")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long id) {
        Optional<Schedule> existingScheduleOpt = scheduleService.getById(id);
        if (existingScheduleOpt.isPresent()) {
            scheduleService.delete(id);

            ApiSuccessResponse<Boolean> response = ApiSuccessResponse.<Boolean>builder()
                    .message("Schedule deleted successfully")
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
                .message("Schedule not found")
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
        return linkTo(methodOn(ScheduleController.class).getAllSchedules()).withRel("collection").withType("GET");
    }

    private Link createLink() {
        return linkTo(methodOn(ScheduleController.class).createSchedule(null)).withRel("create").withType("POST");
    }

    private Link coursesLink() {
        return linkTo(methodOn(CourseController.class).getAllCourses()).withRel("courses").withType("GET");
    }

    private Link selfLink(Long id) {
        return linkTo(methodOn(ScheduleController.class).getScheduleById(id)).withSelfRel().withType("GET");
    }

    private Link updateLink(Long id) {
        return linkTo(methodOn(ScheduleController.class).updateSchedule(id, null)).withRel("update").withType("PUT");
    }

    private Link deleteLink(Long id) {
        return linkTo(methodOn(ScheduleController.class).deleteSchedule(id)).withRel("delete").withType("DELETE");
    }
}
