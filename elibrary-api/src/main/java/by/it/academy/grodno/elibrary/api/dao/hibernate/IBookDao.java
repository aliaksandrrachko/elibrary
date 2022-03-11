package by.it.academy.grodno.elibrary.api.dao.hibernate;

import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IBookDao extends IAGenericDao<Book, Long>{

    Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable);

    Page<Book> findAllByTitleLike(String title, Pageable pageable);

    int deleteByIsbn(String isbn);

    void updateDescription(Book book);

    Page<Book> findAllBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
