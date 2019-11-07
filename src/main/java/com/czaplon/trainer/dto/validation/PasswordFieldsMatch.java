package com.czaplon.trainer.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordFieldsMatchValidator.class)
@Documented
public @interface PasswordFieldsMatch {
String message() default "Fields must match";
Class<?>[] groups() default {};
Class<? extends Payload>[] payload() default {};
}
