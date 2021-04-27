package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.CategoryJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.mappers.BookMapper;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class BookService implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

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
        return bookOptional.map(book -> bookMapper.toDto(book));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Book> bookOptional = bookJpaRepository.findById(id);
        bookOptional.ifPresent(book -> bookJpaRepository.delete(book));
    }

    @Override
    @Transactional
    public Optional<BookDto> create(BookDto entityDto) {
        entityDto.setCreated(LocalDateTime.now().withNano(0));
        entityDto.setUpdated(LocalDateTime.now().withNano(0));
        Book createdBook = bookJpaRepository.saveAndFlush(bookMapper.toEntity(entityDto));
        return Optional.of(bookMapper.toDto(createdBook));
    }

    @Override
    @Transactional
    public Optional<BookDto> update(Long id, BookDto entityDto) {
        Optional<Book> optionalBook = bookJpaRepository.findById(id);
        if (optionalBook.isPresent()){
            Book bookFromDb = optionalBook.get();
            entityDto.setCreated(bookFromDb.getCreated());
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            Book newDataBook = bookMapper.toEntity(entityDto);
            newDataBook.setId(id);
            Book updatedBook = bookJpaRepository.save(newDataBook);
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
}