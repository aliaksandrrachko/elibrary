package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper extends AGenericMapper<Publisher, PublisherDto, Integer> {

    protected PublisherMapper(ModelMapper modelMapper) {
        super(modelMapper, Publisher.class, PublisherDto.class);
    }
}
