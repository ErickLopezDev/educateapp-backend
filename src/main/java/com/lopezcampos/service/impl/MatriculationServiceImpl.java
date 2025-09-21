package com.lopezcampos.service.impl;

import org.springframework.stereotype.Service;

import com.lopezcampos.dto.MatriculationDto;
import com.lopezcampos.dto.request.MatriculationRequestDto;
import com.lopezcampos.dto.response.MatriculationResponseDto;
import com.lopezcampos.exception.matriculations.InvalidAcademicPeriodException;
import com.lopezcampos.model.Matriculation;
import com.lopezcampos.repository.MatriculationRepository;
import com.lopezcampos.service.interface_.AbstractCrudService;

@Service
public class MatriculationServiceImpl
        extends AbstractCrudService<Matriculation, Long, MatriculationRequestDto, MatriculationResponseDto, MatriculationRepository>{

    public MatriculationServiceImpl(MatriculationRepository matriculationRepository) {
        super(matriculationRepository, Matriculation.class, MatriculationResponseDto.class);
    }

    @Override
    public MatriculationResponseDto create(MatriculationRequestDto requestDto) {
        validateAcademicPeriod(requestDto);
        return super.create(requestDto);
    }

    @Override
    public MatriculationResponseDto update(Long id, MatriculationRequestDto requestDto) {
        validateAcademicPeriod(requestDto);
        return super.update(id, requestDto);
    }

    private void validateAcademicPeriod(MatriculationRequestDto requestDto) {
        if (requestDto.getAcademicPeriod() == null || requestDto.getAcademicPeriod().trim().isEmpty()) {
            throw new InvalidAcademicPeriodException("Academic period is required");
        }
    }
}
