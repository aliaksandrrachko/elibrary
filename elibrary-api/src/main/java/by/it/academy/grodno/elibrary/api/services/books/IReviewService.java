package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface IReviewService {

    ReviewDto findById(Long id);
    void delete(Long id);
    ReviewDto create(ReviewDto entityDto);
    ReviewDto update(Long id, ReviewDto entityDto);
    ReviewDto findByBookIdAndUserId(Long bookId, Long userId);
    Page<ReviewDto> findByBookIdAndUserId(Long bookId, Long userId, Pageable pageable);
    Page<ReviewDto> findByBookId(Long bookId, Pageable pageable);
    int totalCountForBook(Long bookId);
    Page<ReviewDto> findByUserIdAndUpdatedBetween(Long userId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);
    Page<ReviewDto> findByBookIdAndUpdatedBetween(Long bookId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);
    Page<ReviewDto> findAll(Pageable pageable);
    boolean existsByIdAndUserId(Long reviewId, Long valueOf);
}
