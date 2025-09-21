package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.EvaluationDto;
import com.lopezcampos.exception.evaluations.NegativeGradeException;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.repository.EvaluationRepository;

@Service
public class EvaluationServiceImpl 
        extends AbstractCrudService<Evaluation, Long, EvaluationDto, EvaluationRepository>{

    public EvaluationServiceImpl(EvaluationRepository evaluationRepository) {
        super(evaluationRepository, Evaluation.class, EvaluationDto.class);
    }

    @Override
    public EvaluationDto create(EvaluationDto evaluationDto) {
        if (evaluationDto.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new NegativeGradeException();
        }
        return super.create(evaluationDto); // ahora devuelve DTO
    }

    @Override
    public EvaluationDto update(Long id, EvaluationDto evaluationDto) {
        if (evaluationDto.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new NegativeGradeException();
        }
        return super.update(id, evaluationDto);
    }
}
