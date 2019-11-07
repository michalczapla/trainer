package com.czaplon.trainer.dto.validation;

import com.czaplon.trainer.dto.RegistrationForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordFieldsMatchValidator implements ConstraintValidator<PasswordFieldsMatch,Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        RegistrationForm form = (RegistrationForm) o;
        return form.getPassword().equals(form.getConfirmPassword());
    }

    @Override
    public void initialize(PasswordFieldsMatch constraintAnnotation) {

    }
}
