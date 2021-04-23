package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "rest/books")
public class BookRestController {

    private final IBookService bookService;

    public BookRestController(IBookService userService) {
        this.bookService = userService;
    }

    @GetMapping()
    public List<BookDto> findAllBook() {
        return bookService.findAll();
    }

    @GetMapping(value = "/{id}")
    public BookDto findBookById(@PathVariable Long id) {
        return bookService.findById(id).orElse(null);
    }

/*    @GetMapping
    public Page<BookDto> findBookBySection(@RequestParam(value = "section") String section,
                                           @PageableDefault Pageable pageable){
        return bookService.findAllBySectionName(section, pageable);
    }*/

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto createBook(@Valid @RequestBody BookDto dto) {
        return bookService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BookDto updateBook(@PathVariable Long id, @Valid @RequestBody BookDto dto) {
        return bookService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
