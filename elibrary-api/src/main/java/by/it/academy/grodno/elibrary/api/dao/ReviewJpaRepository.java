package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    int countByBookId(Long bookId);

    @EntityGraph(value = "review-user-book-entity-graph")
    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByUserIdAndUpdatedBetween(Long userId, LocalDateTime updated, LocalDateTime updated2, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByUserIdAndUpdatedBefore(Long userId, LocalDateTime updated, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByUserIdAndUpdatedAfter(Long userId, LocalDateTime updated, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByBookIdAndUpdatedBefore(Long bookId, LocalDateTime updated, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByBookIdAndUpdatedAfter(Long bookId, LocalDateTime updated, Pageable pageable);

    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findByBookIdAndUpdatedBetween(Long bookId, LocalDateTime updated, LocalDateTime updated2, Pageable pageable);

    boolean existsByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT AVG(r.grade) FROM Review r WHERE r.book.id = :bookId")
    Integer countAverageGradeByBookId(@Param("bookId") Long bookId);

    @Override
    @EntityGraph(value = "review-user-book-entity-graph")
    Page<Review> findAll(Pageable pageable);
}
