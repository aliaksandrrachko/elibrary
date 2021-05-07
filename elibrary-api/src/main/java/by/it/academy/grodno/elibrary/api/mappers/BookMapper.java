package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.PublisherJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class BookMapper extends AGenericMapper<Book, BookDto, Long> {

    private final PublisherJpaRepository publisherJpaRepository;

    private final CategoryJpaRepository categoryJpaRepository;

    private final CategoryMapper categoryMapper;

    protected BookMapper(ModelMapper modelMapper, PublisherJpaRepository publisherJpaRepository,
                         CategoryJpaRepository categoryJpaRepository, CategoryMapper categoryMapper) {
        super(modelMapper, Book.class, BookDto.class);
        this.publisherJpaRepository = publisherJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
        this.categoryMapper = categoryMapper;
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
