package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    // http://localhost:8080/rest/authors/pages?page=1&size=5 :request example
    @GetMapping("/pages")
    public List<AuthorDto> findAllAuthors(@PageableDefault Pageable pageable) {
        return authorService.findAll(pageable).getContent();
    }

    @GetMapping(value = "/{id}")
    public AuthorDto findAuthor(@PathVariable Integer id) {
        return authorService.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto createAuthor(@RequestBody AuthorDto dto) {
        return authorService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto updateAuthor(@RequestBody AuthorDto dto, @PathVariable Integer id) {
        return authorService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteAuthor(@PathVariable Integer id) {
        authorService.delete(id);
    }
}
