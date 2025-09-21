package com.lopezcampos.exception.evaluations;

import com.lopezcampos.exception.base.BusinessException;

public class NegativeGradeException extends BusinessException {
    public NegativeGradeException() {
        super("Grade cannot be negative", "EVAL_001");
    }
}