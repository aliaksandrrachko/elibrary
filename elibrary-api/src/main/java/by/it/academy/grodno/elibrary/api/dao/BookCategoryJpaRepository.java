package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookCategoryJpaRepository extends JpaRepository<BookCategory, Integer> {
}
