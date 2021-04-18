package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "rest/publisher")
public class PublisherRestController {

    private final IPublisherService publisherService;

    public PublisherRestController(IPublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping()
    public List<PublisherDto> findAllSection() {
        return publisherService.findAll();
    }

    @GetMapping(value = "/{id}")
    public PublisherDto findSection(@PathVariable Integer id) {
        return publisherService.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto createCategory(@RequestBody PublisherDto dto) {
        return publisherService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto updateCategory(@RequestBody PublisherDto dto, @PathVariable Integer id) {
        return publisherService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteSection(@PathVariable Integer id) {
        publisherService.delete(id);
    }

}
