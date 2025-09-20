package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Evaluation;
import com.lopezcampos.repository.EvaluationRepository;
import com.lopezcampos.service.interface_.EvaluationService;

@Service
public class EvaluationServiceImpl extends AbstractCrudService<Evaluation, Long, EvaluationRepository> implements EvaluationService {

    public EvaluationServiceImpl(EvaluationRepository evaluationRepository) {
        super(evaluationRepository);
    }

    @Override
    public Evaluation create(Evaluation evaluation) {
        if (evaluation.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Grade cannot be negative");
        }
        return super.create(evaluation);
    }

    @Override
    public Evaluation update(Long id, Evaluation evaluation) {
        if (evaluation.getGrade().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Grade cannot be negative");
        }
        return super.update(id, evaluation);
    }
}