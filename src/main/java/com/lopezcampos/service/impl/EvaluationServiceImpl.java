package com.lopezcampos.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.request.EvaluationRequestDto;
import com.lopezcampos.dto.response.EvaluationResponseDto;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.exception.evaluations.NegativeGradeException;
import com.lopezcampos.model.Course;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.model.Student;
import com.lopezcampos.repository.EvaluationRepository;
import com.lopezcampos.repository.MatriculationRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class EvaluationServiceImpl
        extends AbstractCrudService<Evaluation, Long, EvaluationRequestDto, EvaluationResponseDto, EvaluationRepository> {

    private final MatriculationRepository matriculationRepository;

    public EvaluationServiceImpl(EvaluationRepository repository, MatriculationRepository matriculationRepository) {
        super(repository, Evaluation.class, EvaluationResponseDto.class);
        this.matriculationRepository = matriculationRepository;
    }

    @Override
    public EvaluationResponseDto create(EvaluationRequestDto requestDto) {
        if (requestDto.getGrade().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeGradeException();
        }

        Evaluation evaluation = ModelMapperConfig.map(requestDto, Evaluation.class);

        Matriculation matriculation = matriculationRepository.findById(requestDto.getMatriculationId())
                .orElseThrow(() -> new NotFoundException("Matriculation not found with id " + requestDto.getMatriculationId()));
        evaluation.setMatriculation(matriculation);

        Evaluation saved = repository.save(evaluation);
        return buildResponse(saved);
    }

    @Override
    public EvaluationResponseDto update(Long id, EvaluationRequestDto requestDto) {
        Evaluation evaluation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evaluation not found with id " + id));

        evaluation.setTypeEvaluation(requestDto.getTypeEvaluation());
        evaluation.setDate(requestDto.getDate());
        evaluation.setGrade(requestDto.getGrade());

        Matriculation matriculation = matriculationRepository.findById(requestDto.getMatriculationId())
                .orElseThrow(() -> new NotFoundException("Matriculation not found with id " + requestDto.getMatriculationId()));
        evaluation.setMatriculation(matriculation);

        Evaluation updated = repository.save(evaluation);
        return buildResponse(updated);
    }

    private EvaluationResponseDto buildResponse(Evaluation evaluation) {
        EvaluationResponseDto dto = new EvaluationResponseDto();
        dto.setIdEvaluation(evaluation.getIdEvaluation());
        dto.setTypeEvaluation(evaluation.getTypeEvaluation());
        dto.setDate(evaluation.getDate());
        dto.setGrade(evaluation.getGrade());

        if (evaluation.getMatriculation() != null) {
            Student student = evaluation.getMatriculation().getStudent();
            Course course = evaluation.getMatriculation().getCourse();

            if (student != null) {
                dto.setStudentName(student.getName());
                dto.setStudentSurname(student.getSurname());
            }
            if (course != null) {
                dto.setCourseName(course.getName());
                dto.setCourseCode(course.getCode());
            }
        }
        return dto;
    }


}
