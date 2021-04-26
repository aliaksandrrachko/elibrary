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
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "books")
public class BookController {

    private final IBookService bookService;
    private final IUserService userService;
    private final ICategoryService categoryService;

    public BookController(IBookService bookService, IUserService userService, ICategoryService categoryService) {
        this.bookService = bookService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView findAllBook(Principal principal, @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @PageableDefault Pageable pageable) {
        UserDto currentUser = userService.findUser(principal).orElse(null);

        Page<BookDto> pageBookDto = bookService.findAll(categoryId, pageable);
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/booksList");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
        return modelAndView;
    }

    @GetMapping(value = "/{id}")
    public ModelAndView bookInfo(Principal principal, @PathVariable Long id){
        UserDto currentUser = userService.findUser(principal).orElse(null);

        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());

        BookDto bookDto = bookService.findById(id).orElse(null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/bookInfo");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("book", bookDto);
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        return modelAndView;
    }
}
