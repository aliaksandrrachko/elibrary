package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(value = "/admin/books")
public class AdminBookController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IPublisherService publisherService;
    @Autowired
    private IAuthorService authorService;

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

    @PostMapping("/setAvailability")
    public ModelAndView setBookAvailability(@Valid @Min(0) long bookId) {
        bookService.setAvailability(bookId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/books?bookId=" + bookId);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getAddBookFormStepOne(@RequestParam int countAuthors,
                                                Principal principal) {
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);

        ModelAndView modelAndView = new ModelAndView();
        if (currentUser == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "Authentication error!");
        } else {
            modelAndView.setViewName("admin/addBookForm");
            modelAndView.addObject("currentUser", currentUser);
            BookDto bookDto = new BookDto();
            bookDto.setAuthors(getBlankListOfString(countAuthors));
            bookDto.setCategory(new CategoryDto());
            modelAndView.addObject("bookDto", bookDto);
            prepareModelAndViewForAddBookForm(modelAndView);
        }
        return modelAndView;
    }

    private List<String> getBlankListOfString(int size){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("");
        }
        return list;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@Valid @ModelAttribute(value = "bookDto") BookDto bookDto,
                                BindingResult result,
                                Principal principal) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/books");
        if (principal == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "You don't have permission");
        } else if (result.hasErrors()) {
            modelAndView.setViewName("admin/addBookForm");
            modelAndView.addAllObjects(result.getModel());
            UserDto currentUser = userService.findById(principal.getName()).orElse(new UserDto());
            modelAndView.addObject("currentUser", currentUser);
            modelAndView.addObject("bookDto", bookDto);
            prepareModelAndViewForAddBookForm(modelAndView);
        } else {
            Optional<BookDto> optionalAddedBook = bookService.create(bookDto);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/info/" + dto.getId()));
        }
        return modelAndView;
    }

    private void prepareModelAndViewForAddBookForm(ModelAndView modelAndView){
        modelAndView.addObject("categoriesSet", categoryService.findAllUnique());
        modelAndView.addObject("publishersList", publisherService.findAll());
        modelAndView.addObject("authorsList", authorService.findAll());
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
    }
}
