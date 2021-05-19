package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "books")
public class BookController extends ABookController{

    private final IReviewService reviewService;

    public BookController(IBookService bookService, IUserService userService, ICategoryService categoryService,
                          IReviewService reviewService) {
        super(bookService, userService, categoryService);
        this.reviewService = reviewService;
    }

    private static final String MODEL_ATTRIBUTE_NAME_CURRENT_USER = "currentUser";

    @Override
    @GetMapping
    public ModelAndView findAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable,
                                    Principal principal) {
        ModelAndView modelAndView = super.findAllBooks(categoryId, title, author, pageable, principal);
        modelAndView.setViewName("books/booksList");
        return modelAndView;
    }

    @GetMapping(value = "/{id}")
    public ModelAndView bookInfo(Principal principal, @PathVariable Long id) {
        UserDto currentUser = userService.findUser(principal);
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());
        int totalCountOfReview = reviewService.totalCountForBook(id);
        BookDto bookDto = bookService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/bookInfo");
        modelAndView.addObject( MODEL_ATTRIBUTE_NAME_CURRENT_USER, currentUser);
        modelAndView.addObject("book", bookDto);
        modelAndView.addObject("totalCountOfReview", totalCountOfReview);
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        return modelAndView;
    }
}
