package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorJpaRepository extends JpaRepository<Author, Integer> {

    List<Author> getByAuthorNameLike(String authorNameLikeExpression);
    Optional<Author> getByAuthorName(String authorName);
}
