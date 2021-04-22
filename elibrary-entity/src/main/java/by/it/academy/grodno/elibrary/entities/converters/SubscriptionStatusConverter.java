package by.it.academy.grodno.elibrary.entities.converters;

import by.it.academy.grodno.elibrary.entities.books.SubscriptionStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SubscriptionStatusConverter implements AttributeConverter<SubscriptionStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SubscriptionStatus attribute) {
        return attribute.getStatusCode();
    }

    @Override
    public SubscriptionStatus convertToEntityAttribute(Integer dbData) {
        return SubscriptionStatus.getSubscriptionStatus(dbData);
    }
}
