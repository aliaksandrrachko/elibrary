package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(value = "book-category-publisher-authors-entity-graph")
    Page<Book> findAll(Pageable pageable);

    Page<Book> findAllByCategoryId(Integer categoryId, Pageable pageable);

    Page<Book> findAllByAuthorsIn(Collection<Author> authors, Pageable pageable);

    Page<Book> findAllByTitleContaining(String title, Pageable pageable);

    Page<Book> findByCategoryIn(Collection<Category> allCategoryIncludeSubCategories, Pageable pageable);
}
