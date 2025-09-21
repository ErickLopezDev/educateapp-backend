package com.lopezcampos.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.lopezcampos.config.ModelMapperConfig;
import com.lopezcampos.dto.request.EvaluationRequestDto;
import com.lopezcampos.dto.response.EvaluationResponseDto;
import com.lopezcampos.exception.base.NotFoundException;
import com.lopezcampos.exception.evaluations.NegativeGradeException;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.model.Matriculation;
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

        EvaluationResponseDto dto = new EvaluationResponseDto();
        dto.setIdEvaluation(saved.getIdEvaluation());
        dto.setTypeEvaluation(saved.getTypeEvaluation());
        dto.setDate(saved.getDate());
        dto.setGrade(saved.getGrade());

        dto.setStudentName(saved.getMatriculation().getStudent().getName());
        dto.setStudentSurname(saved.getMatriculation().getStudent().getSurname());
        dto.setCourseName(saved.getMatriculation().getCourse().getName());
        dto.setCourseCode(saved.getMatriculation().getCourse().getCode());

        return dto;
    }

}
