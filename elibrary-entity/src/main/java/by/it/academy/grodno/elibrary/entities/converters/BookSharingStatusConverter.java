package by.it.academy.grodno.elibrary.entities.converters;

import by.it.academy.grodno.elibrary.entities.books.BookSharingStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BookSharingStatusConverter implements AttributeConverter<BookSharingStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BookSharingStatus attribute) {
        return attribute.getStatusCode();
    }

    @Override
    public BookSharingStatus convertToEntityAttribute(Integer dbData) {
        return BookSharingStatus.getBookSharingStatus(dbData);
    }
}
