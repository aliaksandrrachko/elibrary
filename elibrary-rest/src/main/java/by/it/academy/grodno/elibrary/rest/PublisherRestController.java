package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "rest/publisher")
public class PublisherRestController {

    private final IPublisherService publisherService;

    public PublisherRestController(IPublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping()
    public List<PublisherDto> findAllPublisher() {
        return publisherService.findAll();
    }

    @GetMapping(value = "/{id}")
    public PublisherDto findPublisher(@PathVariable Integer id) {
        return publisherService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto createPublisher(@Valid @RequestBody PublisherDto dto) {
        return publisherService.create(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto updatePublisher(@Valid @RequestBody PublisherDto dto, @PathVariable Integer id) {
        return publisherService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deletePublisher(@PathVariable Integer id) {
        publisherService.delete(id);
    }

}
