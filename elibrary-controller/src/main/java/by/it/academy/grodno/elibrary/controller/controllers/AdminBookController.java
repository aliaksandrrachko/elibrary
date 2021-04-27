package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/admin/books")
public class AdminBookController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ModelAndView findAllBook(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "bookId", required = false) Long bookId,
                                    @RequestParam(value = "authorName", required = false) String authorName,
                                    @PageableDefault Pageable pageable,
                                    Principal principal) {
        UserDto currentUser = userService.findUser(principal).orElse(null);

        Page<BookDto> pageBookDto = bookService.findAll(categoryId, pageable);
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminBooksList");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
        return modelAndView;
    }
}
