package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Author.ADMIN_AUTHORS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Author.ADMIN_AUTHORS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Author.AUTHORS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Author.AUTHORS_HAS_THE_MOST_BOOK;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Author.AUTHORS_ID;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AuthorRestController {

    private final IAuthorService authorService;

    public AuthorRestController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(AUTHORS)
    public Page<AuthorDto> findAllAuthors(@PageableDefault(sort = {"authorName"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return authorService.findAll(pageable);
    }

    @GetMapping(value = AUTHORS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto findAuthor(@PathVariable Integer id) {
        return authorService.findById(id);
    }

    @GetMapping(value = AUTHORS_HAS_THE_MOST_BOOK, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorDto> findAllAuthorWhoHasTheMostBooks(){
        return authorService.findWhoHasTheMostBooks();
    }

    @PostMapping(value = ADMIN_AUTHORS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto createAuthor(@Valid @RequestBody AuthorDto dto) {
        return authorService.create(dto);
    }

    @PutMapping(value = ADMIN_AUTHORS_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AuthorDto updateAuthor(@Valid @RequestBody AuthorDto dto, @PathVariable Integer id) {
        return authorService.update(id, dto);
    }

    @DeleteMapping(value = ADMIN_AUTHORS_ID)
    public void deleteAuthor(@PathVariable Integer id) {
        authorService.delete(id);
    }
}
