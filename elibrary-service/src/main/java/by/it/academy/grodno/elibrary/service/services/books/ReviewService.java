package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.ReviewJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.exceptions.UserTryCreateMoreThanOneReviewForBookException;
import by.it.academy.grodno.elibrary.api.mappers.ReviewMapper;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.entities.books.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class ReviewService implements IReviewService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewJpaRepository reviewJpaRepository, ReviewMapper reviewMapper) {
        this.reviewJpaRepository = reviewJpaRepository;
        this.reviewMapper = reviewMapper;
    }


    @Override
    public ReviewDto findById(Long id) {
        Optional<Review> reviewOptional = reviewJpaRepository.findById(id);
        return reviewOptional.map(reviewMapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        Optional<Review> reviewOptional = reviewJpaRepository.findById(id);
        reviewOptional.ifPresent(reviewJpaRepository::delete);
    }

    @Override
    public ReviewDto create(ReviewDto entityDto) {
        if (reviewJpaRepository.existsByUserIdAndBookId(entityDto.getUserId(), entityDto.getBookId())){
            throw new UserTryCreateMoreThanOneReviewForBookException(entityDto.getUserId(), entityDto.getBookId());
        }
        Review review = reviewMapper.toEntity(entityDto);
        review.setCreated(LocalDateTime.now().withNano(0));
        review.setUpdated(LocalDateTime.now().withNano(0));
        review = reviewJpaRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto update(Long id, ReviewDto entityDto) {
        Optional<Review> reviewOptional = reviewJpaRepository.findById(id);
        if (!reviewOptional.isPresent()){
            return null;
        }
        Review review = reviewOptional.get();
        review.setGrade(entityDto.getGrade());
        review.setText(entityDto.getText());
        review.setUpdated(LocalDateTime.now().withNano(0));
        review = reviewJpaRepository.save(review);
        return reviewMapper.toDto(review);
    }

    @Override
    public ReviewDto findByBookIdAndUserId(Long bookId, Long userId) {
        return reviewMapper.toDto(reviewJpaRepository.findByBookIdAndUserId(bookId, userId).orElse(null));
    }

    @Override
    public Page<ReviewDto> findByBookId(Long bookId, Pageable pageable) {
        return reviewMapper.toPageDto(reviewJpaRepository.findByBookId(bookId, pageable));
    }

    @Override
    public int totalCountForBook(Long bookId) {
        if (bookId == null || bookId < 0){
            return 0;
        }
        return reviewJpaRepository.countByBookId(bookId);
    }

    @Override
    public Page<ReviewDto> findByUserIdAndUpdatedBetween(Long userId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        Page<Review> reviewPage;
        if (dateFrom == null && dateTo == null){
            reviewPage = reviewJpaRepository.findByUserId(userId, pageable);
        } else if (dateFrom == null){
            reviewPage = reviewJpaRepository.findByUserIdAndUpdatedBefore(userId, LocalDateTime.of(dateTo, LocalTime.of(0,0)), pageable);
        } else if (dateTo == null){
            reviewPage = reviewJpaRepository.findByUserIdAndUpdatedAfter(userId, LocalDateTime.of(dateFrom, LocalTime.of(0,0)), pageable);
        } else {
            reviewPage = reviewJpaRepository.findByUserIdAndUpdatedBetween(userId,
                    LocalDateTime.of(dateFrom, LocalTime.of(0,0)),
                    LocalDateTime.of(dateTo, LocalTime.of(0,0)),
                    pageable);
        }
        return reviewMapper.toPageDto(reviewPage);
    }

    @Override
    public Page<ReviewDto> findByBookIdAndUpdatedBetween(Long bookId, LocalDate dateFrom, LocalDate dateTo, Pageable pageable) {
        Page<Review> reviewPage;
        if (dateFrom == null && dateTo == null){
            reviewPage = reviewJpaRepository.findByBookId(bookId, pageable);
        } else if (dateFrom == null){
            reviewPage = reviewJpaRepository.findByBookIdAndUpdatedBefore(bookId, LocalDateTime.of(dateTo, LocalTime.of(0,0)), pageable);
        } else if (dateTo == null){
            reviewPage = reviewJpaRepository.findByBookIdAndUpdatedAfter(bookId, LocalDateTime.of(dateFrom, LocalTime.of(0,0)), pageable);
        } else {
            reviewPage = reviewJpaRepository.findByBookIdAndUpdatedBetween(bookId,
                    LocalDateTime.of(dateFrom, LocalTime.of(0,0)),
                    LocalDateTime.of(dateTo, LocalTime.of(0,0)),
                    pageable);
        }
        return reviewMapper.toPageDto(reviewPage);
    }

    @Override
    public Page<ReviewDto> findAll(Pageable pageable) {
        return reviewMapper.toPageDto(reviewJpaRepository.findAll(pageable));
    }

    @Override
    public boolean existsByIdAndUserId(Long reviewId, Long userId) {
        return reviewJpaRepository.existsByIdAndUserId(reviewId, userId);
    }
}
