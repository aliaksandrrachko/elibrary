package by.it.academy.grodno.elibrary.api.validations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Isbn10Validator.class)
@Target(value = {ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn10 {
    String message() default "Invalid ISBN-10";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
