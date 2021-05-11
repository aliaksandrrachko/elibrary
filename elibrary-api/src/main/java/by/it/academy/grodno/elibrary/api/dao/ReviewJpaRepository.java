package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.entities.books.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {

    Page<Review> findByUserId(Long userId, Pageable pageable);
    Page<Review> findByBookId(Long bookId, Pageable pageable);
    int countByBookId(Long bookId);
    Optional<ReviewDto> findByBookIdAndUserId(Long bookId, Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    Page<Review> findByUserIdAndCreatedBetween(Long userId, LocalDateTime created, LocalDateTime created2, Pageable pageable);
}
