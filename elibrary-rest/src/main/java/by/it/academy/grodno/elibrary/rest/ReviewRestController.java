package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//@RestController
@RequestMapping(value = "rest/reviews")
public class ReviewRestController {

    public final IReviewService reviewService;

    public ReviewRestController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/forbook/{bookId}")
    public Page<ReviewDto> findAllReviewForBook(@PathVariable Long bookId, @PageableDefault Pageable pageable) {
        return reviewService.findByBookId(bookId, pageable);
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
