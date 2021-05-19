package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequestMapping(value = "/admin/reviews")
public class AdminReviewController {

    private final IUserService userService;
    private final IReviewService reviewService;

    public AdminReviewController(IUserService userService, IReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }

    private static final String MODEL_ATTRIBUTE_NAME_CURRENT_USER = "currentUser";

    @GetMapping
    public ModelAndView findAll(@RequestParam(value = "bookId", required = false) Long bookId,
                                @RequestParam(value = "userId", required = false) Long userId,
                                @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
                                @RequestParam(value = "dateTo", required = false) LocalDate dateTo,
                                @PageableDefault(sort = {"updated"}, direction = Sort.Direction.ASC) Pageable pageable,
                                Principal principal){
        Page<ReviewDto> reviewDtoPage;
        if (bookId != null && userId != null){
            reviewDtoPage = reviewService.findByBookIdAndUserId(bookId, Long.valueOf(principal.getName()), pageable);
        } else if (bookId != null){
            reviewDtoPage = reviewService.findByBookIdAndUpdatedBetween(bookId, dateFrom, dateTo, pageable);
        } else if (userId != null){
            reviewDtoPage = reviewService.findByUserIdAndUpdatedBetween(userId, dateFrom, dateTo, pageable);
        } else {
            reviewDtoPage = reviewService.findAll(pageable);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminReviewsList");
        modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, userService.findById(principal.getName()));
        modelAndView.addObject("dateFrom", dateFrom);
        modelAndView.addObject("dateTo", dateTo);
        modelAndView.addObject("currentBookId", bookId);
        modelAndView.addObject("currentUserId", bookId);
        modelAndView.addObject("reviewDtoPage", reviewDtoPage);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(reviewDtoPage.getNumber(), reviewDtoPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping("/delete/{reviewId}")
    public ModelAndView deleteReview(@PathVariable Long reviewId){
        ModelAndView modelAndView = new ModelAndView();
        reviewService.delete(reviewId);
        modelAndView.setViewName("redirect:/admin/reviews");
        return modelAndView;
    }

    @PostMapping("/update/{reviewId}")
    public ModelAndView updateReview(@PathVariable Long reviewId,
                                     @ModelAttribute ReviewDto reviewDto,
                                     BindingResult result,
                                     Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        if (result.hasErrors()) {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.CURRENT_USER, userService.findById(principal.getName()));
            modelAndView.addObject(result.getModel());
        } else {
            ReviewDto updatedReview = reviewService.update(reviewId, reviewDto);
            if (updatedReview != null) {
                modelAndView.setViewName("redirect:/admin/reviews" + "?bookId=" + updatedReview.getBookId() +
                        "&userId=" + updatedReview.getUserId());
            }
        }
        return modelAndView;
    }
}
