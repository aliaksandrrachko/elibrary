package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.books.ReviewDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.exceptions.UserTryCreateMoreThanOneReviewForBookException;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.controller.constants.Template;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "books")
public class BookController {

    private final IBookService bookService;
    private final IUserService userService;
    private final ICategoryService categoryService;
    private final IReviewService reviewService;

    public BookController(IBookService bookService, IUserService userService, ICategoryService categoryService,
                          IReviewService reviewService) {
        this.bookService = bookService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    private static final String MODEL_ATTRIBUTE_NAME_CURRENT_USER = "currentUser";

    @GetMapping
    public ModelAndView findAllBook(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable,
                                    Principal principal) {
        Page<BookDto> pageBookDto;
        CategoryDto categoryDto = null;
        if (categoryId != null) {
            pageBookDto = bookService.findAllIncludeSubCategories(categoryId, pageable);
            categoryDto = categoryService.findById(categoryId);
        } else if (title != null) {
            pageBookDto = bookService.findAllByTitle(title, pageable);
        } else if (author != null) {
            pageBookDto = bookService.findAllByAuthorName(author, pageable);
        } else {
            pageBookDto = bookService.findAll(pageable);
        }

        Set<CategoryDto> categoryDtoSet = new HashSet<>(categoryService.findAll());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/booksList");
        modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, userService.findUser(principal));
        setBooksViewAttributesToModelAndView(modelAndView, categoryId, title, author, pageBookDto,
                categoryDto, categoryDtoSet);
        return modelAndView;
    }

    static void setBooksViewAttributesToModelAndView(ModelAndView modelAndView, Integer categoryId, String title, String author, Page<BookDto> pageBookDto,
                                                     CategoryDto categoryDto, Set<CategoryDto> categoryDtoList) {
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("categoryId", categoryId);
        modelAndView.addObject("currentCategoryDto", categoryDto);
        modelAndView.addObject("title", title);
        modelAndView.addObject("author", author);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
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
        if (principal == null || !reviewDto.getUserId().equals(Long.valueOf(principal.getName()))){
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.ERROR_TEXT, "You don't have permission");
            modelAndView.addObject(Template.Error.TIMESTAMP, LocalDateTime.now().withNano(0));
        } else if (result.hasErrors()){
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

    private void setNeedingObjectToModelAndView(ModelAndView modelAndView, Principal principal, Long bookId, Pageable pageable){
        UserDto currentUser = userService.findUser(principal);
        modelAndView.addObject( MODEL_ATTRIBUTE_NAME_CURRENT_USER, currentUser);
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
