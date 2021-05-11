package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "rest/authors")
public class AuthorRestController {

    private final IAuthorService authorService;

    public AuthorRestController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    public List<AuthorDto> findAllAuthors() {
        return authorService.findAll();
    }

    @GetMapping("/pages")
    public Page<AuthorDto> findAllAuthors(@PageableDefault Pageable pageable) {
        return authorService.findAll(pageable);
    }

    @GetMapping(value = "/{id}")
    public AuthorDto findAuthor(@PathVariable Integer id) {
        return authorService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorDto dto) {
        return authorService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto updateAuthor(@Valid @RequestBody AuthorDto dto, @PathVariable Integer id) {
        return authorService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteAuthor(@PathVariable Integer id) {
        authorService.delete(id);
    }
}
