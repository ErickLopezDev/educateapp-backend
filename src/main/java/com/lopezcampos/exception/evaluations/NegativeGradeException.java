package com.lopezcampos.service.impl;

import com.lopezcampos.exception.BusinessException;

public class NegativeGradeException extends BusinessException {
    public NegativeGradeException() {
        super("Grade cannot be negative", "EVAL_003");
    }
}