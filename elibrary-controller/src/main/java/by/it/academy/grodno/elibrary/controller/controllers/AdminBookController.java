package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import by.it.academy.grodno.elibrary.api.utils.IsbnUtils;
import by.it.academy.grodno.elibrary.controller.constants.Template;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/books")
public class AdminBookController extends ABookController{

    private static final String TEMPLATE_INPUT_BOOK_FORM = "admin/inputBookDetailsForm";
    private static final String MODEL_ATTRIBUTE_NAME_BOOK_DTO = "bookDto";

    private final IPublisherService publisherService;
    private final IAuthorService authorService;

    public AdminBookController(IUserService userService, IBookService bookService, ICategoryService categoryService,
                               IPublisherService publisherService, IAuthorService authorService) {
        super(bookService, userService, categoryService);
        this.publisherService = publisherService;
        this.authorService = authorService;
    }

    @Override
    @GetMapping
    public ModelAndView findAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "author", required = false) String author,
                                    @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable,
                                    Principal principal) {
        ModelAndView modelAndView = super.findAllBooks(categoryId, title, author, pageable, principal);
        modelAndView.setViewName("admin/adminBooksList");
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
    public ModelAndView getAddBookForm(@RequestParam(value = "countAuthors", required = false, defaultValue = "3") int countAuthors,
                                       @RequestParam(value = "countAttributes", required = false, defaultValue = "3") int countAttributes,
                                       @RequestParam(value = "isbn", required = false) String isbn,
                                       Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);

        if (isbn != null && IsbnUtils.isValid(isbn)) {
            Optional<BookDto> optionalBookDto = bookService.findByIsbnInWeb(IsbnUtils.getOnlyDigit(isbn));
            BookDto bookDto;
            if (optionalBookDto.isPresent()) {
                bookDto = optionalBookDto.get();
                addEmptyStringToList(bookDto.getAuthors(), countAuthors);
                convertMapToFormInputFormat(bookDto.getAttributes(), countAttributes);
            } else {
                modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
                modelAndView.addObject(Template.Error.ERROR_TEXT, "Resource didn't find.");
                String message = String.format("Book by isbn: '%s' didn't find. Try few later.", isbn);
                modelAndView.addObject("message", message);
                modelAndView.addObject("timestamp", LocalDateTime.now().withNano(0));
                return modelAndView;
            }
            modelAndView.setViewName(TEMPLATE_INPUT_BOOK_FORM);
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_BOOK_DTO, bookDto);
        } else {
            modelAndView.setViewName(TEMPLATE_INPUT_BOOK_FORM);
            BookDto bookDto = new BookDto();
            bookDto.setAuthors(addEmptyStringToList(bookDto.getAuthors(), countAuthors));
            bookDto.setAttributes(convertMapToFormInputFormat(bookDto.getAttributes(), countAttributes));
            bookDto.setCategory(new CategoryDto());
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_BOOK_DTO, bookDto);
        }
        prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        return modelAndView;
    }

    private Map<String, String> convertMapToFormInputFormat(Map<String, String> attributes, int size) {
        List<String> paramList = attributes.entrySet().stream().map(entry -> entry.getKey() + ':' + entry.getValue())
                .collect(Collectors.toList());
        for (int i = 0; i < size; i++) {
            paramList.add("");
        }
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < paramList.size(); i++) {
            result.put("param" + i, paramList.get(i));
        }
        return result;
    }

    private Map<String, String> convertToSimpleMap(Map<String, String> attributes) {
        return attributes.values().stream()
                .filter(o -> StringUtils.hasText((CharSequence) o) && o.matches("^.+:.+$"))
                .collect(Collectors.toList())
                .stream().collect(Collectors.toMap(
                        key -> key.substring(0, key.indexOf(":")),
                        value -> value.substring(value.indexOf(":") + 1)));
    }

    private List<String> addEmptyStringToList(List<String> authors, int size) {
        for (int i = 0; i < size; i++) {
            authors.add("");
        }
        return authors;
    }

    @PostMapping("/add")
    public ModelAndView addBook(@Valid @ModelAttribute(value = "bookDto") BookDto bookDto,
                                BindingResult result,
                                @RequestParam(value = "fileBookCover", required = false) MultipartFile file,
                                Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("redirect:/books");
        if (result.hasErrors()) {
            modelAndView.setViewName(TEMPLATE_INPUT_BOOK_FORM);
            modelAndView.addAllObjects(result.getModel());
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_BOOK_DTO, bookDto);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        } else {
            bookDto.setAttributes(convertToSimpleMap(bookDto.getAttributes()));
            Optional<BookDto> optionalAddedBook = bookService.create(bookDto, file);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/" + dto.getId()));
        }
        return modelAndView;
    }

    private void prepareModelAndViewForAddAndUpdateBookForm(ModelAndView modelAndView) {
        modelAndView.addObject("categoriesSet", categoryService.findAllUnique());
        modelAndView.addObject("publishersList", publisherService.findAll());
        modelAndView.addObject("authorsList", authorService.findAll());
        Set<CategoryDto> categoryDtoList = new HashSet<>(categoryService.findAll());
        modelAndView.addObject("categoryDtoSet", categoryDtoList);
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
                                          @RequestParam(value = "countAttributes", required = false, defaultValue = "3") int countAttributes,
                                          Principal principal) {
        BookDto bookDtoForUpdate = bookService.findById(bookId);

        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        if (bookDtoForUpdate == null) {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.ERROR_TEXT, "Book not exists!");
        } else {
            modelAndView.setViewName(TEMPLATE_INPUT_BOOK_FORM);
            bookDtoForUpdate.setAuthors(addEmptyStringToList(bookDtoForUpdate.getAuthors(), countAuthors));
            bookDtoForUpdate.setAttributes(convertMapToFormInputFormat(bookDtoForUpdate.getAttributes(), countAttributes));
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_BOOK_DTO, bookDtoForUpdate);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        }
        return modelAndView;
    }

    @PostMapping("/update/{bookId}")
    public ModelAndView updateBook(@PathVariable @Valid @Min(0) long bookId,
                                   @Valid @ModelAttribute(value = "bookDto") BookDto bookDto, BindingResult result,
                                   @RequestParam(value = "fileBookCover", required = false) MultipartFile file,
                                   Principal principal) {
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("redirect:/books");
        if (result.hasErrors()) {
            modelAndView.setViewName(TEMPLATE_INPUT_BOOK_FORM);
            modelAndView.addAllObjects(result.getModel());
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_BOOK_DTO, bookDto);
            prepareModelAndViewForAddAndUpdateBookForm(modelAndView);
        } else {
            bookDto.setAttributes(convertToSimpleMap(bookDto.getAttributes()));
            Optional<BookDto> optionalAddedBook = bookService.update(bookId, bookDto, file);
            optionalAddedBook.ifPresent(dto -> modelAndView.setViewName("redirect:/books/" + dto.getId()));
        }
        return modelAndView;
    }
}
