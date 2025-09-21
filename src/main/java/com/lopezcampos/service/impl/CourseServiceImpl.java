package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.request.CourseRequestDto;
import com.lopezcampos.dto.response.CourseResponseDto;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Teacher;
import com.lopezcampos.repository.CourseRepository;
import com.lopezcampos.repository.TeacherRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class CourseServiceImpl 
        extends AbstractCrudService<Course, Long, CourseRequestDto, CourseResponseDto, CourseRepository> {

    private final TeacherRepository teacherRepository;

    public CourseServiceImpl(CourseRepository repository, TeacherRepository teacherRepository) {
        super(repository, Course.class, CourseResponseDto.class);
        this.teacherRepository = teacherRepository;
    }

    @Override
    public CourseResponseDto create(CourseRequestDto requestDto) {
        Course course = ModelMapperConfig.map(requestDto, Course.class);

        Teacher teacher = teacherRepository.findById(requestDto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with id " + requestDto.getTeacherId()));

        course.setTeacher(teacher);

        Course saved = repository.save(course);
        return ModelMapperConfig.map(saved, CourseResponseDto.class);
    }

    @Override
    public CourseResponseDto update(Long id, CourseRequestDto requestDto) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Course not found with id " + id));

        course.setName(requestDto.getName());
        course.setCode(requestDto.getCode());
        course.setCredits(requestDto.getCredits());
        course.setSemester(requestDto.getSemester());

        Teacher teacher = teacherRepository.findById(requestDto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found with id " + requestDto.getTeacherId()));
        course.setTeacher(teacher);

        Course updated = repository.save(course);
        return ModelMapperConfig.map(updated, CourseResponseDto.class);
    }
}
