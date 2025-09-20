package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.model.Matriculation;
import com.lopezcampos.repository.MatriculationRepository;
import com.lopezcampos.service.interface_.MatriculationService;

@Service
public class MatriculationServiceImpl extends AbstractCrudService<Matriculation, Long, MatriculationRepository> implements MatriculationService {

    private final MatriculationRepository matriculationRepository;

    public MatriculationServiceImpl(MatriculationRepository matriculationRepository) {
        super(matriculationRepository);
        this.matriculationRepository = matriculationRepository;
    }

    @Override
    public Matriculation create(Matriculation matriculation) {
        if (matriculation.getAcademicPeriod() == null || matriculation.getAcademicPeriod().trim().isEmpty()) {
            throw new RuntimeException("Academic period is required");
        }
        return super.create(matriculation);
    }

    @Override
    public Matriculation update(Long id, Matriculation matriculation) {
        if (matriculation.getAcademicPeriod() == null || matriculation.getAcademicPeriod().trim().isEmpty()) {
            throw new RuntimeException("Academic period is required");
        }
        return super.update(id, matriculation);
    }
}