package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.PublisherJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class BookMapper extends AGenericMapper<Book, BookDto, Long> {

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    protected BookMapper(ModelMapper modelMapper) {
        super(modelMapper, Book.class, BookDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Book.class, BookDto.class)
                .addMappings(u -> {
                    u.skip(BookDto::setAuthors);
                    u.skip(BookDto::setPublisher);
                    u.skip(BookDto::setCategory);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(BookDto.class, Book.class)
                .addMappings(m -> {
                    m.skip(Book::setAuthors);
                    m.skip(Book::setPublisher);
                    m.skip(Book::setCategory);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Book source, BookDto destination) {
        destination.setAuthors(source.getAuthors().stream().map(Author::getAuthorName).collect(Collectors.toList()));
        destination.setPublisher(source.getPublisher().getPublisherName());
        destination.setCategory(categoryMapper.toDto(source.getCategory()));
    }

    @Override
    public void mapSpecificFields(BookDto source, Book destination) {
        destination.setPublisher(publisherJpaRepository.findByPublisherName(source.getPublisher())
                .orElse(null));
        destination.setCategory(categoryJpaRepository.findByCategoryName(source.getCategory().getCategoryName())
                .orElseThrow(NoSuchElementException::new));
    }
}
