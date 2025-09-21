package com.lopezcampos.exception.matriculations;

import com.lopezcampos.exception.base.BusinessException;

public class InvalidAcademicPeriodException extends BusinessException {
    public InvalidAcademicPeriodException(String period) {
        super("Invalid academic period: " + period, "MATR_001");
    }
}