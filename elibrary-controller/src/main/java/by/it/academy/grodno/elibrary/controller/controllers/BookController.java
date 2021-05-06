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
    public ModelAndView findAllBook(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @PageableDefault Pageable pageable,
                                    Principal principal) {
        UserDto currentUser = userService.findUser(principal).orElse(null);

        Page<BookDto> pageBookDto;
        if (categoryId != null){
            pageBookDto = bookService.findAllIncludeSubCategories(categoryId, pageable);
        } else if (title != null) {
            pageBookDto = bookService.findAllByTitle(title, pageable);
        } else if (author != null){
            pageBookDto = bookService.findAllByAuthorName(author, pageable);
        } else {
            pageBookDto = bookService.findAll(pageable);
        }

        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/booksList");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("categoryId", categoryId);
        modelAndView.addObject("title", title);
        modelAndView.addObject("author", author);
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
