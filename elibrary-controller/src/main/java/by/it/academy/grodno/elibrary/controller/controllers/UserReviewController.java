package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.controller.constants.Template;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/reviews")
public class UserReviewController {

    private final IUserService userService;
    private final IReviewService reviewService;

    public UserReviewController(IUserService userService, IReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    private static final String MODEL_ATTRIBUTE_NAME_CURRENT_USER = "currentUser";

    @GetMapping
    public ModelAndView findAll(@RequestParam(value = "bookId", required = false) Long bookId,
                                @RequestParam(value = "dateFrom", required = false)LocalDate dateFrom,
                                @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
                                @PageableDefault(sort = {"created"}, direction = Sort.Direction.ASC) Pageable pageable,
                                Principal principal){
        Page<ReviewDto> reviewDtoPage;
        if (bookId != null){
            ReviewDto reviewDto = reviewService.findByBookIdAndUserId(bookId, Long.valueOf(principal.getName()));
            List<ReviewDto> reviewDtos;
            if (reviewDto == null) {
                reviewDtos = Collections.emptyList();
            } else {
                reviewDtos = Collections.singletonList(reviewDto);
            }
            reviewDtoPage = new PageImpl<>(reviewDtos, pageable, 1L);
        } else {
            reviewDtoPage = reviewService.findByUserIdAndUpdatedBetween(Long.valueOf(principal.getName()), dateFrom, dateTo, pageable);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/usersReviewsList");
        modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, userService.findById(principal.getName()));
        modelAndView.addObject("dateFrom", dateFrom);
        modelAndView.addObject("dateTo", dateTo);
        modelAndView.addObject("currentBookId", bookId);
        modelAndView.addObject("reviewDtoPage", reviewDtoPage);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(reviewDtoPage.getNumber(), reviewDtoPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping("/delete/{reviewId}")
    public ModelAndView deleteReview(@PathVariable Long reviewId,
                                     Principal principal){
        ReviewDto reviewDto = reviewService.findById(reviewId);
        ModelAndView modelAndView = new ModelAndView();
        if (reviewDto.getUserId().equals(Long.valueOf(principal.getName()))){
            reviewService.delete(reviewId);
            modelAndView.setViewName("redirect:/reviews");
        } else {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.CURRENT_USER, userService.findById(principal.getName()));
            modelAndView.addObject(Template.Error.ERROR_TEXT, "You don't have permission");
        }
        return modelAndView;
    }

    @PostMapping("/update/{reviewId}")
    public ModelAndView updateReview(@PathVariable Long reviewId,
                                     @ModelAttribute ReviewDto reviewDto,
                                     BindingResult result,
                                     Principal principal){
        boolean userHasReview = reviewService.existsByIdAndUserId(reviewId, Long.valueOf(principal.getName()));
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors() || !userHasReview) {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.CURRENT_USER, userService.findById(principal.getName()));
            modelAndView.addObject(Template.Error.TIMESTAMP, LocalDateTime.now().withNano(0));
            modelAndView.addObject(result.getModel());
        } else {
            ReviewDto updatedReview = reviewService.update(reviewId, reviewDto);
            if (updatedReview != null) {
                modelAndView.setViewName("redirect:/reviews" + "?bookId=" + updatedReview.getBookId());
            }
        }
        return modelAndView;
    }
}
