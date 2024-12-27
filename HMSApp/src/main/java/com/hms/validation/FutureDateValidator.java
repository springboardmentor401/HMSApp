package com.hms.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FutureDateValidator implements ConstraintValidator<ValidFutureDate, LocalDate> {

    @Override
    public void initialize(ValidFutureDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate appDate, ConstraintValidatorContext context) {
        if (appDate == null) {
            return true;
        }
        LocalDate today = LocalDate.now();
        LocalDate maxFutureDate = today.plusYears(1);
        return !appDate.isAfter(maxFutureDate);
    }
}
