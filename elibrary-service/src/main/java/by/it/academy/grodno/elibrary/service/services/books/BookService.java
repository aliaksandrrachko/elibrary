package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.mappers.BookMapper;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.utils.BookDataProvider;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import by.it.academy.grodno.elibrary.entities.utils.IsbnUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
public class BookService implements IBookService {

    private final BookMapper bookMapper;
    private final AuthorJpaRepository authorJpaRepository;
    private final BookJpaRepository bookJpaRepository;

    public BookService(BookMapper bookMapper, AuthorJpaRepository authorJpaRepository, BookJpaRepository bookJpaRepository) {
        this.bookMapper = bookMapper;
        this.authorJpaRepository = authorJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
    }

    @Override
    public Class<BookDto> getGenericClass() {
        return BookDto.class;
    }

    @Override
    public List<BookDto> findAll() {
        return bookMapper.toDtos(bookJpaRepository.findAll());
    }

    @Override
    public Optional<BookDto> findById(Long id) {
        Optional<Book> bookOptional = bookJpaRepository.findById(id);
        return bookOptional.map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Book> bookOptional = bookJpaRepository.findById(id);
        bookOptional.ifPresent(bookJpaRepository::delete);
    }

    @Override
    @Transactional
    public Optional<BookDto> create(BookDto entityDto) {
        entityDto.setCreated(LocalDateTime.now().withNano(0));
        entityDto.setUpdated(LocalDateTime.now().withNano(0));
        Book newBookData = bookMapper.toEntity(entityDto);
        createAndSetPublisherIfNotExists(newBookData, entityDto);
        createAndSetAuthorIfNotExists(newBookData, entityDto);
        Book createdBook = bookJpaRepository.save(newBookData);
        return Optional.of(bookMapper.toDto(createdBook));
    }

    private void createAndSetPublisherIfNotExists(Book book, BookDto bookDto){
        if (book.getPublisher() == null) {
            Publisher publisher = new Publisher(bookDto.getPublisher());
            book.setPublisher(publisher);
        }
    }

    private void createAndSetAuthorIfNotExists(Book book, BookDto bookDto){
        Set<String> booksAuthors = book.getAuthors().stream().map(Author::getAuthorName)
                .collect(Collectors.toSet());
        bookDto.getAuthors().forEach(authorName -> {
            if (!booksAuthors.contains(authorName) && StringUtils.hasText(authorName)){
                book.getAuthors().add(new Author(authorName));
            }
        });
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto entityDto) {
        Optional<Book> optionalBook = bookJpaRepository.findById(id);
        if (optionalBook.isPresent()){
            Book bookFromDb = optionalBook.get();
            entityDto.setCreated(bookFromDb.getCreated());
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            Book newBookData = bookMapper.toEntity(entityDto);
            newBookData.setId(id);
            createAndSetPublisherIfNotExists(newBookData, entityDto);
            createAndSetAuthorIfNotExists(newBookData, entityDto);
            Book updatedBook = bookJpaRepository.save(newBookData);
            return Optional.of(bookMapper.toDto(updatedBook));
        }
        return Optional.empty();
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAll(pageable));
    }

    @Override
    public Page<BookDto> findAllByCategoryId(Integer categoryId, Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAllByCategoryId(categoryId, pageable));
    }

    @Override
    public Page<BookDto> findAll(Integer categoryId, Pageable pageable) {
        if (categoryId == null) {
            return findAll(pageable);
        } else {
            return findAllByCategoryId(categoryId, pageable);
        }
    }

    @Override
    public Page<BookDto> findAllByTitle(String title, Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAllByTitleContaining(title, pageable));
    }

    @Override
    public Page<BookDto> findAllByAuthorName(String author, Pageable pageable) {
        List<Author> authors = authorJpaRepository.findByAuthorNameContaining(author);
        return bookMapper.toPageDto(bookJpaRepository.findAllByAuthorsIn(authors, pageable));
    }

    @Override
    @Transactional
    public void setAvailability(long bookId) {
        Optional<Book> bookOptional = bookJpaRepository.findById(bookId);
        bookOptional.ifPresent(book -> {
            book.setAvailable(!book.isAvailable());
            bookJpaRepository.save(book);
        });
    }

    @Autowired
    private BookDataProvider bookDataWebProvider;

    @Override
    public Optional<BookDto> findByIsbnInWeb(@NotNull String isbn) {
        String isbn13 = IsbnUtils.toIsbn13(isbn);
        return bookDataWebProvider.getBook(isbn13).map(bookMapper::toDto);
    }
}