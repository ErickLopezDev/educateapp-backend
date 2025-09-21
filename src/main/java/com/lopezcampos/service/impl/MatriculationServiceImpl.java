package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.exception.matriculations.InvalidAcademicPeriodException;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.repository.MatriculationRepository;
import com.lopezcampos.service.interface_.MatriculationService;

@Service
public class MatriculationServiceImpl extends AbstractCrudService<Matriculation, Long, MatriculationRepository> implements MatriculationService {

    public MatriculationServiceImpl(MatriculationRepository matriculationRepository) {
        super(matriculationRepository);
    }

    @Override
    public Matriculation create(Matriculation matriculation) {
        validateAcademicPeriod(matriculation);
        return super.create(matriculation);
    }

    @Override
    public Matriculation update(Long id, Matriculation matriculation) {
        validateAcademicPeriod(matriculation);
        return super.update(id, matriculation);
    }

    private void validateAcademicPeriod(Matriculation matriculation) {
        if (matriculation.getAcademicPeriod() == null || matriculation.getAcademicPeriod().trim().isEmpty()) {
            throw new InvalidAcademicPeriodException("Academic period is required");
        }
    }
}