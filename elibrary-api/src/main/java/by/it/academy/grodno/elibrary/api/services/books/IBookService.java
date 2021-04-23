package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookService extends IAGenericCrudService<BookDto, Long> {

    Page<BookDto> findAllBySectionName(String sectionName, Pageable pageable);
}
