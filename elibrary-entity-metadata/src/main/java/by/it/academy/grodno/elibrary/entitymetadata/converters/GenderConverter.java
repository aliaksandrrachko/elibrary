package by.it.academy.grodno.elibrary.entitymetadata.converters;

import by.it.academy.grodno.elibrary.entitymetadata.users.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        return gender == null ? null : gender.getAbbreviation();
    }

    @Override
    public Gender convertToEntityAttribute(Character abbreviation) {
        return Gender.getGender(abbreviation);
    }
}
