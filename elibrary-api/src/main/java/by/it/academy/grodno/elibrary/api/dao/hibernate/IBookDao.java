package by.it.academy.grodno.elibrary.api.dao.hibernate;

import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBookDao extends IAGenericDao<Book, Long>{

    Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable);

    Page<Book> findAllByTitleLike(String title, Pageable pageable);
}
