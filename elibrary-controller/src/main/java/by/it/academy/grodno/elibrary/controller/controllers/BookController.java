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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "books")
public class BookController {

    @Autowired
    private IBookService bookService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ModelAndView findAllBook(Principal principal, @PageableDefault Pageable pageable) {
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);
        Page<BookDto> pageBookDto = bookService.findAll(pageable);
        List<CategoryDto> categoryDtoList = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/booksList");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("categoryDtoList", categoryDtoList);
        modelAndView.addObject("pageBookDto", pageBookDto);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
        return modelAndView;
    }

    @GetMapping(value = "/{id}")
    public ModelAndView bookInfo(Principal principal, @PathVariable Long id){
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);
        BookDto bookDto = bookService.findById(id).orElse(null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("books/bookInfo");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("book", bookDto);
        return modelAndView;
    }
// add some logic in another method
//    @GetMapping
//    public ModelAndView findAllBookBySection(Principal principal, @RequestParam(value = "section") String section,
//                                             @PageableDefault Pageable pageable){
//        UserDto currentUser = userService.findById(principal.getName()).orElse(null);
//        ModelAndView modelAndView = new ModelAndView();
//        List<CategoryDto> categoryDtoList = categoryService.findAll();
//        Page<BookDto> pageBookDto = bookService.findAllBySectionName(section, pageable);
//        modelAndView.setViewName("books/booksList");
//        modelAndView.addObject("currentUser", currentUser);
//        modelAndView.addObject("categoryDtoList", categoryDtoList);
//        modelAndView.addObject("pageBookDto", pageBookDto);
//        modelAndView.addObject("pageNumbers",
//                PageNumberListCreator.getListOfPagesNumber(pageBookDto.getNumber(), pageBookDto.getTotalPages()));
//        return modelAndView;
//    }

}
