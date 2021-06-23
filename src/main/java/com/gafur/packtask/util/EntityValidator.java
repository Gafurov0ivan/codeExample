package com.gafur.packtask.util;

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class EntityValidator {
    private static Validator validator;

    @PostConstruct
    public void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static <T> void validate(T entity) {
        Set<ConstraintViolation<T>> errors = validator.validate(entity);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
    }
}
