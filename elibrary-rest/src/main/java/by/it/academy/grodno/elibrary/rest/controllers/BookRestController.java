package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Book.ADMIN_BOOKS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Book.ADMIN_BOOKS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Book.BOOKS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Book.BOOKS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Book.BOOKS_ISBN;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.utils.IsbnUtils;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class BookRestController {

    private final IBookService bookService;

    public BookRestController(IBookService userService) {
        this.bookService = userService;
    }

    @GetMapping(value = BOOKS)
    public Page<BookDto> findAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "author", required = false) String author,
                                      @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.findAllBooks(categoryId, title, author, pageable);
    }

    @GetMapping(value = BOOKS_ID)
    public BookDto findBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping(value = BOOKS_ISBN)
    public BookDto findBookByIsbnInWeb(@PathVariable @Valid @ISBN(type = ISBN.Type.ANY) String isbn) {
        return this.bookService.findByIsbnInWeb(IsbnUtils.getOnlyDigit(isbn)).orElse(null);
    }

    @PostMapping(value = ADMIN_BOOKS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto createBook(@Valid @RequestBody BookDto dto) {
        return bookService.create(dto);
    }

    @PutMapping(value = ADMIN_BOOKS_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody BookDto dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping(value = ADMIN_BOOKS_ID)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
