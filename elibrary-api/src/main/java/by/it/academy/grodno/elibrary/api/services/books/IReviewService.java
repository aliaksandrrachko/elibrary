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
    Page<ReviewDto> findByUserId(Long userId, Pageable pageable);
    Page<ReviewDto> findByBookId(Long bookId, Pageable pageable);
    int totalCountForBook(Long bookId);
    Page<ReviewDto> findByUserIdAndCreatedBetween(Long userId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable);
}
