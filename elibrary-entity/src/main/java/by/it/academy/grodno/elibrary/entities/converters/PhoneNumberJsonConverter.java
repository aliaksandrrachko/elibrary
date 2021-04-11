package by.it.academy.grodno.elibrary.entities.converters;

import by.it.academy.grodno.elibrary.entities.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Converter;

@Converter
@Slf4j
public class PhoneNumberJsonConverter extends AGenericJsonConverter<PhoneNumber> {

    protected PhoneNumberJsonConverter() {
        super(PhoneNumber.class);
    }
}
