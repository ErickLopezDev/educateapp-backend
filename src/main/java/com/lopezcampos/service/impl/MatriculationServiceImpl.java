package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.request.MatriculationRequestDto;
import com.lopezcampos.dto.response.MatriculationResponseDto;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.model.Student;
import com.lopezcampos.repository.CourseRepository;
import com.lopezcampos.repository.MatriculationRepository;
import com.lopezcampos.repository.StudentRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class MatriculationServiceImpl
        extends AbstractCrudService<Matriculation, Long, MatriculationRequestDto, MatriculationResponseDto, MatriculationRepository> {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public MatriculationServiceImpl(MatriculationRepository repository,
                                    StudentRepository studentRepository,
                                    CourseRepository courseRepository) {
        super(repository, Matriculation.class, MatriculationResponseDto.class);
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public MatriculationResponseDto create(MatriculationRequestDto requestDto) {

        Matriculation matriculation = new Matriculation();
        matriculation.setAcademicPeriod(requestDto.getAcademicPeriod());
        matriculation.setMatriculationDate(requestDto.getMatriculationDate());
        matriculation.setMatriculationStatus(requestDto.getMatriculationStatus());

        Student student = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id " + requestDto.getStudentId()));
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id " + requestDto.getCourseId()));

        matriculation.setStudent(student);
        matriculation.setCourse(course);

        Matriculation saved = repository.save(matriculation);

        return buildResponseDto(saved);
    }

    @Override
    public MatriculationResponseDto update(Long id, MatriculationRequestDto requestDto) {

        Matriculation existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Matriculation not found with id " + id));

        existing.setAcademicPeriod(requestDto.getAcademicPeriod());
        existing.setMatriculationDate(requestDto.getMatriculationDate());
        existing.setMatriculationStatus(requestDto.getMatriculationStatus());

        Student student = studentRepository.findById(requestDto.getStudentId())
                .orElseThrow(() -> new NotFoundException("Student not found with id " + requestDto.getStudentId()));
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with id " + requestDto.getCourseId()));

        existing.setStudent(student);
        existing.setCourse(course);

        Matriculation saved = repository.save(existing);

        return buildResponseDto(saved);
    }

    private MatriculationResponseDto buildResponseDto(Matriculation matriculation) {
        MatriculationResponseDto dto = ModelMapperConfig.map(matriculation, MatriculationResponseDto.class);

        dto.setStudentName(matriculation.getStudent().getName());
        dto.setStudentSurname(matriculation.getStudent().getSurname());
        dto.setCourseName(matriculation.getCourse().getName());
        dto.setCourseCode(matriculation.getCourse().getCode());

        return dto;
    }
}
