package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.utils.IsbnUtils;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/rest/books")
public class BookRestController {

    private final IBookService bookService;

    public BookRestController(IBookService userService) {
        this.bookService = userService;
    }

    @GetMapping()
    public Page<BookDto> findAllBook(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                     @PageableDefault Pageable pageable) {
        return bookService.findAll(categoryId, pageable);
    }

    @GetMapping
    public Page<BookDto> findAllBooks(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "author", required = false) String author,
                                      @PageableDefault(sort = {"title"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return bookService.findAllBooks(categoryId, title, author, pageable);
    }

    @GetMapping(value = "/{id}")
    public BookDto findBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto createBook(@Valid @RequestBody BookDto dto) {
        return bookService.create(dto);
    }

    @GetMapping
    public BookDto findBookByIsbnInWeb(@Valid @ISBN(type = ISBN.Type.ANY) String isbn,
                                       BindingResult result,
                                       HttpServletResponse response) {
        if (result.hasErrors()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            Optional<BookDto> bookDtoOptional = this.bookService.findByIsbnInWeb(IsbnUtils.getOnlyDigit(isbn));
            if (bookDtoOptional.isPresent()) {
                return bookDtoOptional.get();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        return null;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody BookDto dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
