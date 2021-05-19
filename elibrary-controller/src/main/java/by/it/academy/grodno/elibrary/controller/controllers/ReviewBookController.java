package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.exceptions.UserTryCreateMoreThanOneReviewForBookException;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.controller.constants.Template;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/books")
public class ReviewBookController {

    private static final String TEMPLATE_REVIEWS_LIST = "books/reviewsList";
    private final IUserService userService;
    private final IBookService bookService;
    private final IReviewService reviewService;

    public ReviewBookController(IUserService userService, IBookService bookService, IReviewService reviewService) {
        this.userService = userService;
        this.bookService = bookService;
        this.reviewService = reviewService;
    }

    @GetMapping("/{bookId}/reviews")
    public ModelAndView findAllReviewForBook(@PathVariable @Valid @Min(0) Long bookId,
                                             @PageableDefault(sort = {"created"}, direction = Sort.Direction.ASC) Pageable pageable,
                                             Principal principal) {

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
                                     Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        if (principal == null || !reviewDto.getUserId().equals(Long.valueOf(principal.getName()))) {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.ERROR_TEXT, "You don't have permission");
            modelAndView.addObject(Template.Error.TIMESTAMP, LocalDateTime.now().withNano(0));
        } else if (result.hasErrors()) {
            setNeedingObjectToModelAndView(modelAndView, principal, reviewDto.getBookId(), pageable);
            modelAndView.setViewName(TEMPLATE_REVIEWS_LIST);
            modelAndView.addAllObjects(result.getModel());
        } else {
            try {
                reviewService.create(reviewDto);
                setNeedingObjectToModelAndView(modelAndView, principal, reviewDto.getBookId(), pageable);
                modelAndView.setViewName(TEMPLATE_REVIEWS_LIST);
            } catch (UserTryCreateMoreThanOneReviewForBookException ex) {
                modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
                modelAndView.addObject(Template.Error.ERROR_TEXT,
                        "You already have feedback on this book. See reviews in your profile");
                modelAndView.addObject(Template.Error.TIMESTAMP, LocalDateTime.now().withNano(0));
            }
        }
        return modelAndView;
    }

    private void setNeedingObjectToModelAndView(ModelAndView modelAndView, Principal principal, Long bookId, Pageable pageable) {
        UserDto currentUser = userService.findUser(principal);
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
