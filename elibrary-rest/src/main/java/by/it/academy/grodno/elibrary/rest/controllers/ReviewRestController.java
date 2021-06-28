package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Review.REVIEWS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Review.REVIEWS_ID;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

@RestController
public class ReviewRestController {

    public final IReviewService reviewService;

    public ReviewRestController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = REVIEWS)
    public Page<ReviewDto> findAll(@RequestParam(value = "bookId", required = false) Long bookId,
                                @RequestParam(value = "userId", required = false) Long userId,
                                @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
                                @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
                                @PageableDefault(sort = {"updated"}, direction = Sort.Direction.ASC) Pageable pageable){
        Page<ReviewDto> reviewDtoPage;
        if (bookId != null && userId != null){
            reviewDtoPage = reviewService.findByBookIdAndUserId(bookId, userId, pageable);
        } else if (bookId != null){
            reviewDtoPage = reviewService.findByBookIdAndUpdatedBetween(bookId, dateFrom, dateTo, pageable);
        } else if (userId != null){
            reviewDtoPage = reviewService.findByUserIdAndUpdatedBetween(userId, dateFrom, dateTo, pageable);
        } else {
            reviewDtoPage = reviewService.findAll(pageable);
        }
        return reviewDtoPage;
    }

    @GetMapping(value = REVIEWS_ID)
    public ReviewDto findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    @PostMapping(value = REVIEWS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto createReview(@Valid @RequestBody ReviewDto dto, Principal principal) {
        dto.setUserId(Long.valueOf(principal.getName()));
        return reviewService.create(dto);
    }

    @PutMapping(value = REVIEWS_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto dto, @PathVariable Long id, Principal principal) {
        return reviewService.update(id, dto, Long.valueOf(principal.getName()));
    }

    @DeleteMapping(value = REVIEWS_ID)
    public void deleteReview(@PathVariable Long id, Principal principal) {
        reviewService.delete(id, Long.valueOf(principal.getName()));
    }
}
