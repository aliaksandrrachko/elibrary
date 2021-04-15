package by.it.academy.grodno.elibrary.entities.converters;

import by.it.academy.grodno.elibrary.entities.users.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.getAbbreviation();
    }

    @Override
    public Gender convertToEntityAttribute(Character abbreviation) {
        return Gender.getGender(abbreviation);
    }
}
