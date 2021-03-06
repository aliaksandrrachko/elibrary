package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "rest/books")
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

    @GetMapping(value = "/{id}")
    public BookDto findBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto createBook(@Valid @RequestBody BookDto dto) {
        return bookService.create(dto);
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
