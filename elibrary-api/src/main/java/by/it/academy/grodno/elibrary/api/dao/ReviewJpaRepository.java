package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    Page<Review> findByUserId(Long userId, Pageable pageable);
    Page<Review> findByBookId(Long bookId, Pageable pageable);
    int countByBookId(Long bookId);
    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    Page<Review> findByUserIdAndUpdatedBetween(Long userId, LocalDateTime updated, LocalDateTime updated2, Pageable pageable);
    Page<Review> findByUserIdAndUpdatedBefore(Long userId, LocalDateTime updated, Pageable pageable);
    Page<Review> findByUserIdAndUpdatedAfter(Long userId, LocalDateTime updated, Pageable pageable);
    Page<Review> findByBookIdAndUpdatedBefore(Long bookId, LocalDateTime updated, Pageable pageable);
    Page<Review> findByBookIdAndUpdatedAfter(Long bookId, LocalDateTime updated, Pageable pageable);
    Page<Review> findByBookIdAndUpdatedBetween(Long bookId, LocalDateTime updated, LocalDateTime updated2, Pageable pageable);
    boolean existsByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT AVG(r.grade) FROM Review r WHERE r.book.id = :bookId")
    Integer countAverageGradeByBookId(@Param("bookId") Long bookId);
}
