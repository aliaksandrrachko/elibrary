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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/admin/books")
public class AdminBookController {

    private final IUserService userService;
    private final IBookService bookService;
    private final ICategoryService categoryService;
    private final IPublisherService publisherService;
    private final IAuthorService authorService;

    public AdminBookController(IUserService userService, IBookService bookService, ICategoryService categoryService,
                               IPublisherService publisherService, IAuthorService authorService) {
        this.userService = userService;
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    @GetMapping
    public ModelAndView findAllBook(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @PageableDefault Pageable pageable,
                                    Principal principal) {
        Set<CategoryDto> categoryDtoSet = new HashSet<>(categoryService.findAll());
        Page<BookDto> pageBookDto;
        if (categoryId != null){
            pageBookDto = bookService.findAll(categoryId, pageable);
        } else if (title != null) {
            pageBookDto = bookService.findAllByTitle(title, pageable);
        } else if (author != null){
            pageBookDto = bookService.findAllByAuthorName(author, pageable);
        } else {
            pageBookDto = bookService.findAll(pageable);
        }

        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("admin/adminBooksList");
        modelAndView.addObject("categoryDtoSet", categoryDtoSet);
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
    public ModelAndView getAddBookForm(@RequestParam(value = "countAuthors", required = false) Integer countAuthors,
                                       @RequestParam(value = "isbn", required = false) String isbn,
                                       Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);

        if (isbn != null) {
            Optional<BookDto> optionalBookDto = bookService.findByIsbnInWeb(isbn);
            BookDto bookDto;
            if (optionalBookDto.isPresent()){
                bookDto = optionalBookDto.get();
            } else {
                modelAndView.setViewName("redirect:/error");
                modelAndView.addObject("error", "Resource didn't find.");
                String message = String.format("Book by isbn: '%s' didn't find. Try few later.", isbn);
                modelAndView.addObject("message", message);
                modelAndView.addObject("timestamp", LocalDateTime.now().withNano(0));
                return modelAndView;
            }
            modelAndView.setViewName("admin/inputBookDetailsForm");
            modelAndView.addObject("bookDto", bookDto);
        } else {
            modelAndView.setViewName("admin/inputBookDetailsForm");
            BookDto bookDto = new BookDto();
            bookDto.setAuthors(addEmptyStringToList(bookDto.getAuthors(), countAuthors));
            bookDto.setCategory(new CategoryDto());
            modelAndView.addObject("bookDto", bookDto);
        }
        prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        return modelAndView;
    }

    private List<String> addEmptyStringToList(List<String> authors, int size){
        for (int i = 0; i < size; i++) {
            authors.add("");
        }
        return authors;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@Valid @ModelAttribute(value = "bookDto") BookDto bookDto,
                                BindingResult result,
                                Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("redirect:/books");
       if (result.hasErrors()) {
            modelAndView.setViewName("admin/inputBookDetailsForm");
            modelAndView.addAllObjects(result.getModel());
            modelAndView.addObject("bookDto", bookDto);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        } else {
            Optional<BookDto> optionalAddedBook = bookService.create(bookDto);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/" + dto.getId()));
        }
        return modelAndView;
    }

    private void prepareModelAndViewForAddAndUpdateBookForm(ModelAndView modelAndView){
        modelAndView.addObject("categoriesSet", categoryService.findAllUnique());
        modelAndView.addObject("publishersList", publisherService.findAll());
        modelAndView.addObject("authorsList", authorService.findAll());
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
    }

    private ModelAndView getModelAndViewWithCurrentUserFromDb(Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        UserDto currentUser = null;
        if (principal != null){
            currentUser = userService.findById(principal.getName()).orElse(null);
        }
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }

    @PostMapping("/delete/{bookId}")
    public ModelAndView deleteBook(@PathVariable @Valid @Min(0) long bookId) {
        bookService.delete(bookId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/books");
        return modelAndView;
    }

    @GetMapping("/update/{bookId}")
    public ModelAndView getUpdateBookForm(@PathVariable Long bookId,
                                            @RequestParam(value = "countAuthors", defaultValue = "2") int countAuthors,
                                           Principal principal) {
        BookDto bookDtoForUpdate = bookService.findById(bookId).orElse(null);

        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        if (bookDtoForUpdate == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "Book not exists!");
        } else {
            modelAndView.setViewName("admin/inputBookDetailsForm");
            bookDtoForUpdate.setAuthors(addEmptyStringToList(bookDtoForUpdate.getAuthors(), countAuthors));
            modelAndView.addObject("bookDto", bookDtoForUpdate);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        }
        return modelAndView;
    }

    @PostMapping("/update/{bookId}")
    public ModelAndView updateBook(@PathVariable @Valid @Min(0)  long bookId,
                                    @Valid @ModelAttribute(value = "bookDto") BookDto bookDto,
                                   BindingResult result,
                                   Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("redirect:/books");
        if (result.hasErrors()) {
            modelAndView.setViewName("admin/inputBookDetailsForm");
            modelAndView.addAllObjects(result.getModel());
            modelAndView.addObject("bookDto", bookDto);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        } else {
            Optional<BookDto> optionalAddedBook = bookService.update(bookId, bookDto);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/" + dto.getId()));
        }
        return modelAndView;
    }
}
