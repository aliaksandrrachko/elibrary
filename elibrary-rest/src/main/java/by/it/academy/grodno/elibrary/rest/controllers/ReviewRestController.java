package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/rest/reviews")
public class ReviewRestController {

    public final IReviewService reviewService;

    public ReviewRestController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/forbook/{bookId}")
    public Page<ReviewDto> findAllReviewForBook(@PathVariable Long bookId, @PageableDefault Pageable pageable) {
        return reviewService.findByBookId(bookId, pageable);
    }

    @GetMapping
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

    @GetMapping(value = "/{id}")
    public ReviewDto findById(@PathVariable Long id) {
        return reviewService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto createReview(@Valid @RequestBody ReviewDto dto) {
        return reviewService.create(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReviewDto updateReview(@Valid @RequestBody ReviewDto dto, @PathVariable Long id) {
        return reviewService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
