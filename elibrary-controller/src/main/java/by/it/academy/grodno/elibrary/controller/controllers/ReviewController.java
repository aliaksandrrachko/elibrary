package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;

@RestController
@RequestMapping("books")
public class ReviewController {

    private final IBookService bookService;
    private final IReviewService reviewService;
    private final IUserService userService;

    public ReviewController(IBookService bookService, IReviewService reviewService, IUserService userService) {
        this.bookService = bookService;
        this.reviewService = reviewService;
        this.userService = userService;
    }


    private static final String TEMPLATE_REVIEWS_LIST = "books/reviewsList";

    @GetMapping("/{bookId}/reviews")
    public ModelAndView findAllReviewForBook(@PathVariable @Valid @Min(0) Long bookId,
                                             @PageableDefault(sort = {"created"}, direction = Sort.Direction.ASC) Pageable pageable,
                                             Principal principal){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(TEMPLATE_REVIEWS_LIST);
        setNeedingObjectToModelAndView(modelAndView, principal, bookId, pageable);
        modelAndView.addObject("reviewDto", new ReviewDto());
        return modelAndView;
    }

    @PostMapping("/reviews")
    public ModelAndView createReview(@Valid @ModelAttribute(value = "reviewDto") ReviewDto reviewDto,
                                     BindingResult result,
                                     @PageableDefault(sort = {"created"}, direction = Sort.Direction.ASC) Pageable pageable,
                                     Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        if (principal == null){
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "You don't have permission");
        } else if (result.hasErrors()){
            setNeedingObjectToModelAndView(modelAndView, principal, reviewDto.getBookId(), pageable);
            modelAndView.setViewName(TEMPLATE_REVIEWS_LIST);
            modelAndView.addAllObjects(result.getModel());
        } else {
            reviewService.create(reviewDto);
            setNeedingObjectToModelAndView(modelAndView, principal, reviewDto.getBookId(), pageable);
            modelAndView.setViewName(TEMPLATE_REVIEWS_LIST);
        }
        return modelAndView;
    }

    private void setNeedingObjectToModelAndView(ModelAndView modelAndView, Principal principal, Long bookId, Pageable pageable){
        UserDto currentUser = userService.findUser(principal).orElse(null);
        modelAndView.addObject("currentUser", currentUser);
        BookDto bookDto = bookService.findById(bookId);
        modelAndView.addObject("book", bookDto);
        int totalCountOfReview = reviewService.totalCountForBook(bookId);
        modelAndView.addObject("totalCountOfReview", totalCountOfReview);
        Page<ReviewDto> reviewDtoPage = reviewService.findByBookId(bookId, pageable);
        modelAndView.addObject("reviewDtoPage", reviewDtoPage);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(reviewDtoPage.getNumber(), reviewDtoPage.getTotalPages()));
    }
}
