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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
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

    @PostMapping("/setAvailability")
    public ModelAndView setBookAvailability(@Valid @Min(0) long bookId) {
        bookService.setAvailability(bookId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/books?bookId=" + bookId);
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView getAddBookFormStepOne(Principal principal) {
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);

        ModelAndView modelAndView = new ModelAndView();
        if (currentUser == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "Authentication error!");
        } else {
            Set<CategoryDto> categoriesSet = categoryService.findAllUnique();
            modelAndView.setViewName("admin/addBookFormStepOne");
            modelAndView.addObject("currentUser", currentUser);
            BookDto bookDto = new BookDto();
            bookDto.setCategory(new CategoryDto());
            modelAndView.addObject("bookDto", bookDto);
            modelAndView.addObject("categoriesSet", categoriesSet);
        }
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@Valid @ModelAttribute(value = "bookDto") BookDto bookDto,
                                BindingResult result,
                                Principal principal) {
        if (bookDto == null){
            bookDto = new BookDto();
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/books");
        if (principal == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "You don't have permission");
        } else if (result.hasErrors()) {
            modelAndView.setViewName("admin/addBookFormStepOne");
            modelAndView.addAllObjects(result.getModel());
            UserDto currentUser = userService.findById(principal.getName()).orElse(new UserDto());
            modelAndView.addObject("currentUser", currentUser);
            modelAndView.addObject("bookDto", bookDto);
        } else {
            Optional<BookDto> optionalAddedBook = bookService.create(bookDto);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/info/" + dto.getId()));
        }
        return modelAndView;
    }

/*    @GetMapping("/create")
    public String showCreateForm(Model model) {
        BooksCreationDto booksForm = new BooksCreationDto();

        for (int i = 1; i <= 3; i++) {
            booksForm.addBook(new Book());
        }

        model.addAttribute("form", booksForm);
        return "books/createBooksForm";
    }*/
}
