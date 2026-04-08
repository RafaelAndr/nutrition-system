package com.personal_finance.exception;

public class IncomeNegativeException extends RuntimeException {
    public IncomeNegativeException(String message) {
        super(message);
    }
}
