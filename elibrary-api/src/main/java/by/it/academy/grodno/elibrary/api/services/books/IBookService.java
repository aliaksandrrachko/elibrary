package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IBookService extends IAGenericCrudService<BookDto, Long> {

    Page<BookDto> findAllByCategoryId(Integer categoryId, Pageable pageable);
    Page<BookDto> findAll(Integer categoryId, Pageable pageable);
    Page<BookDto> findAllByTitle(String title, Pageable pageable);
    Page<BookDto> findAllByAuthorName(String author, Pageable pageable);
    void setAvailability(long bookId);
    Optional<BookDto> findByIsbnInWeb(String isbn);
    Page<BookDto> findAllIncludeSubCategories(Integer categoryId, Pageable pageable);
}
