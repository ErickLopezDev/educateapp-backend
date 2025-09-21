package com.lopezcampos.exception;

public class InvalidAcademicPeriodException extends BusinessException {
    public InvalidAcademicPeriodException(String period) {
        super("Invalid academic period: " + period, "MATR_001");
    }
}