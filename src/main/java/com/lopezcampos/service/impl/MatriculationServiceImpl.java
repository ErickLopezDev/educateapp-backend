package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.MatriculationDto;
import com.lopezcampos.exception.matriculations.InvalidAcademicPeriodException;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.repository.MatriculationRepository;

@Service
public class MatriculationServiceImpl
        extends AbstractCrudService<Matriculation, Long, MatriculationDto, MatriculationRepository>{

    public MatriculationServiceImpl(MatriculationRepository matriculationRepository) {
        super(matriculationRepository, Matriculation.class, MatriculationDto.class);
    }

    @Override
    public MatriculationDto create(MatriculationDto dto) {
        validateAcademicPeriod(dto);
        return super.create(dto);
    }

    @Override
    public MatriculationDto update(Long id, MatriculationDto dto) {
        validateAcademicPeriod(dto);
        return super.update(id, dto);
    }

    private void validateAcademicPeriod(MatriculationDto dto) {
        if (dto.getAcademicPeriod() == null || dto.getAcademicPeriod().trim().isEmpty()) {
            throw new InvalidAcademicPeriodException("Academic period is required");
        }
    }
}
