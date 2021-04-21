package by.it.academy.grodno.elibrary.api.validations;

import by.it.academy.grodno.elibrary.entities.utils.IsbnUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class Isbn10Validator implements ConstraintValidator<Isbn10, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && IsbnUtils.isIsbn10(value);
    }
}
