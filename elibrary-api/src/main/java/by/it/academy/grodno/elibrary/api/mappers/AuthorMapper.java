package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.entities.books.Author;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper extends AGenericMapper<Author, AuthorDto, Integer>{

    protected AuthorMapper(ModelMapper modelMapper) {
        super(modelMapper, Author.class, AuthorDto.class);
    }
}
