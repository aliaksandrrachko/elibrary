package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorJpaRepository extends JpaRepository<Author, Integer> {

    List<Author> findByAuthorNameContaining(String authorName);
    Optional<Author> findByAuthorName(String authorName);

    @Query(value = "SELECT a.id, a.author_name FROM author a " +
            "LEFT OUTER JOIN book_has_author bha on a.id = bha.author_id " +
            "INNER JOIN book b on bha.book_id = b.id GROUP BY a.id HAVING COUNT(b.id) = ( " +
            "SELECT MAX(t.count_book) FROM " +
            "(SELECT DISTINCT COUNT(bha.book_id) count_book FROM author a " +
            "LEFT OUTER JOIN book_has_author bha on a.id = bha.author_id GROUP BY a.id) as t);",
            nativeQuery = true)
    List<Author> findWhoHasTheMostBooks();
}
