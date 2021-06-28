package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDetailsDto;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Review;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ReviewMapper extends AGenericMapper<Review, ReviewDto, Long> {

    private final UserJpaRepository userJpaRepository;
    private final BookJpaRepository bookJpaRepository;

    protected ReviewMapper(ModelMapper modelMapper, UserJpaRepository userJpaRepository, BookJpaRepository bookJpaRepository) {
        super(modelMapper, Review.class, ReviewDto.class);
        this.userJpaRepository = userJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Review.class, ReviewDto.class)
                .addMappings(u -> {
                    u.skip(ReviewDto::setUserId);
                    u.skip(ReviewDto::setUsername);
                    u.skip(ReviewDto::setUserEmail);
                    u.skip(ReviewDto::setUserPictureUrl);
                    u.skip(ReviewDto::setBookId);
                    u.skip(ReviewDto::setBookDetails);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(ReviewDto.class, Review.class)
                .addMappings(m -> {
                    m.skip(Review::setBook);
                    m.skip(Review::setUser);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Review source, ReviewDto destination) {
        destination.setUserId(source.getUser().getId());
        destination.setUsername(source.getUser().getUsername());
        destination.setUserEmail(source.getUser().getEmail());
        destination.setUserPictureUrl(source.getUser().getAvatarUrl());
        destination.setBookId(source.getBook().getId());
        destination.setBookDetails(getBookDetailsDtoFromBook(source.getBook()));
    }

    private BookDetailsDto getBookDetailsDtoFromBook(Book book) {
        return BookDetailsDto.builder()
                .authors(book.getAuthors().stream().map(Author::getAuthorName).collect(Collectors.toList()))
                .datePublishing(book.getDatePublishing())
                .pictureUrl(book.getPictureUrl())
                .title(book.getTitle())
                .build();
    }

    @Override
    public void mapSpecificFields(ReviewDto source, Review destination) {
        Optional<Book> bookOptional = bookJpaRepository.findById(source.getBookId());
        destination.setBook(bookOptional.orElseThrow(() ->
                new NoSuchElementException(String.format("Book with id:'%d' doesn't exist", source.getBookId()))));
        Optional<User> userOptional = userJpaRepository.findById(source.getUserId());
        destination.setUser(userOptional.orElseThrow(() ->
                new NoSuchElementException(String.format("User with id:'%d' doesn't exist", source.getUserId()))));

    }
}
