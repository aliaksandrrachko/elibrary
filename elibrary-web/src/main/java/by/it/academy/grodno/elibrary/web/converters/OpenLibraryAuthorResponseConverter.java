package by.it.academy.grodno.elibrary.web.converters;

import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.web.responseentities.OpenLibraryAuthorResponse;
import org.springframework.core.convert.converter.Converter;

public class OpenLibraryAuthorResponseConverter implements Converter<OpenLibraryAuthorResponse, Author> {

    @Override
    public Author convert(OpenLibraryAuthorResponse source) {
        return Author.builder()
                .authorName(source.getAuthorName())
                .build();
    }
}
