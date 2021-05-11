package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/books")
public class AdminReviewController {

    private final IUserService userService;
    private final IReviewService reviewService;

    public AdminReviewController(IUserService userService, IReviewService reviewService) {
        this.userService = userService;
        this.reviewService = reviewService;
    }


}
