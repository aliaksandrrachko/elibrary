package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public abstract class ABookController {

    protected final IBookService bookService;
    protected final IUserService userService;
    protected final ICategoryService categoryService;

    protected ABookController(IBookService bookService, IUserService userService, ICategoryService categoryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView findAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                     @RequestParam(value = "title", required = false) String title,
                                     @RequestParam(value = "author", required = false) String author,
                                     @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable,
                                     Principal principal) {
        Page<BookDto> pageBookDto = bookService.findAllBooks(categoryId, title, author, pageable);
        Set<CategoryDto> categoryDtoSet = new HashSet<>(categoryService.findAll());
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("books/booksList");
        setBooksViewAttributesToModelAndView(modelAndView, categoryId, title, author, pageBookDto, categoryDtoSet);
        return modelAndView;
    }

    protected void setBooksViewAttributesToModelAndView(ModelAndView modelAndView, Integer categoryId, String title,
                                                     String author, Page<BookDto> pageBookDto,
                                                     Set<CategoryDto> categoryDtoList) {
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("categoryId", categoryId);
        modelAndView.addObject("title", title);
        modelAndView.addObject("author", author);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
    }

    protected ModelAndView getModelAndViewWithCurrentUserFromDb(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        UserDto currentUser = null;
        if (principal != null) {
            currentUser = userService.findById(principal.getName());
        }
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }
}
