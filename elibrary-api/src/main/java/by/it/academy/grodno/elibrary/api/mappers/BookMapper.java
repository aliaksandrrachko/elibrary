package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.PublisherJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.SectionJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper extends AGenericMapper<Book, BookDto, Long> {

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Autowired
    private SectionJpaRepository sectionJpaRepository;

    @Autowired
    private SectionMapper sectionMapper;

    protected BookMapper(ModelMapper modelMapper) {
        super(modelMapper, Book.class, BookDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Book.class, BookDto.class)
                .addMappings(u -> {
                    u.skip(BookDto::setAuthors);
                    u.skip(BookDto::setPublisher);
                    u.skip(BookDto::setSection);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(BookDto.class, Book.class)
                .addMappings(m -> {
                    m.skip(Book::setAuthors);
                    m.skip(Book::setPublisher);
                    m.skip(Book::setSection);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Book source, BookDto destination) {
        destination.setAuthors(source.getAuthors().stream().map(Author::getAuthorName).collect(Collectors.toSet()));
        destination.setPublisher(source.getPublisher().getPublisherName());
        destination.setSection(sectionMapper.toDto(source.getSection()));
    }

    @Override
    public void mapSpecificFields(BookDto source, Book destination) {
        Set<Author> authorSet = new HashSet<>();
        source.getAuthors().forEach(a -> {
            Optional<Author> authorOptional = authorJpaRepository.getByAuthorName(a);
            authorOptional.ifPresent(authorSet::add);
        });
        destination.setAuthors(authorSet);
        publisherJpaRepository.findByPublisherName(source.getPublisher()).ifPresent(destination::setPublisher);
        destination.setSection(sectionJpaRepository.findBySectionName(source.getSection().getSectionName()).orElse(null));
    }
}