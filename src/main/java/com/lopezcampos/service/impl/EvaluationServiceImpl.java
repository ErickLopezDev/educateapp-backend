package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.request.EvaluationRequestDto;
import com.lopezcampos.dto.response.EvaluationResponseDto;
import com.lopezcampos.exception.evaluations.NegativeGradeException;
import com.lopezcampos.model.Evaluation;
import com.lopezcampos.repository.EvaluationRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class EvaluationServiceImpl 
        extends AbstractCrudService<Evaluation, Long, EvaluationRequestDto, EvaluationResponseDto, EvaluationRepository> {

    public EvaluationServiceImpl(EvaluationRepository repository) {
        super(repository, Evaluation.class, EvaluationResponseDto.class);
    }

    @Override
    public EvaluationResponseDto create(EvaluationRequestDto requestDto) {
        if (requestDto.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new NegativeGradeException();
        }
        return super.create(requestDto);
    }

    @Override
    public EvaluationResponseDto update(Long id, EvaluationRequestDto requestDto) {
        if (requestDto.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new NegativeGradeException();
        }
        return super.update(id, requestDto);
    }
}
