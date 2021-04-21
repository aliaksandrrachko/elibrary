package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.mappers.BookMapper;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookService implements IBookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookJpaRepository bookJpaRepository;

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
    public void delete(Long id) {
        Optional<Book> bookOptional = bookJpaRepository.findById(id);
        bookOptional.ifPresent(book -> bookJpaRepository.delete(book));
    }

    @Override
    public Optional<BookDto> create(BookDto entityDto) {
        Book createdBook = bookJpaRepository.save(bookMapper.toEntity(entityDto));
        String stop = "stop";
        return Optional.of(bookMapper.toDto(createdBook));
    }

    @Override
    public Optional<BookDto> update(Long id, BookDto entityDto) {
        Optional<Book> optionalBook = bookJpaRepository.findById(id);
        if (!optionalBook.isPresent()){
            return Optional.empty();
        }

        Book newDataBook = bookMapper.toEntity(entityDto);
        newDataBook.setId(id);
        bookJpaRepository.save(newDataBook);
        //mapFields(newDataBook, optionalBook.get());
        return Optional.empty();
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookMapper.toPageDto(bookJpaRepository.findAll(pageable));
    }

    private void mapFields(Book source, Book destination){
        destination.setAuthors(source.getAuthors());
        destination.setAvailable(source.isAvailable());
        destination.setAvailableCount(source.getAvailableCount());
        destination.setDatePublishing(source.getDatePublishing());
        destination.setDescription(source.getDescription());
        destination.setIsbn10(source.getIsbn10());
        destination.setIsbn13(source.getIsbn13());
        destination.setLanguage(source.getLanguage());
        destination.setPictureUrl(source.getPictureUrl());
        destination.setPrintLength(source.getPrintLength());
        destination.setPublisher(source.getPublisher());
        destination.setSection(source.getSection());
        destination.setTitle(source.getTitle());
        destination.setTotalCount(source.getTotalCount());
    }
}
