package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable);

    Page<Book> findAllByAuthorsIn(Collection<Author> authors, Pageable pageable);

    Page<Book> findAllByTitleContaining(String title, Pageable pageable);
}
