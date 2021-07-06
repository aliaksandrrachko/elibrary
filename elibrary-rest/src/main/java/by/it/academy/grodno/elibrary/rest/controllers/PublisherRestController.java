package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Publisher.ADMIN_PUBLISHERS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Publisher.ADMIN_PUBLISHERS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Publisher.PUBLISHERS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Publisher.PUBLISHERS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Publisher.PUBLISHERS_NAME_LIKE;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PublisherRestController {

    private final IPublisherService publisherService;

    public PublisherRestController(IPublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping(value = PUBLISHERS)
    public List<PublisherDto> findAllPublisher() {
        return publisherService.findAll();
    }

    @GetMapping(value = PUBLISHERS_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto findPublisher(@PathVariable Integer id) {
        return publisherService.findById(id);
    }

    @GetMapping(value = PUBLISHERS_NAME_LIKE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<PublisherDto> findPublisherWithBookByPublisherNameLike(@PathVariable String publisherName,
                                                                 @PageableDefault Pageable pageable){
        return publisherService.findAllPublisherWithBookByPublisherNameLike(publisherName, pageable);
    }

    @PostMapping(value = ADMIN_PUBLISHERS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto createPublisher(@Valid @RequestBody PublisherDto dto) {
        return publisherService.create(dto);
    }

    @PutMapping(value = ADMIN_PUBLISHERS_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PublisherDto updatePublisher(@Valid @RequestBody PublisherDto dto, @PathVariable Integer id) {
        return publisherService.update(id, dto);
    }

    @DeleteMapping(value = ADMIN_PUBLISHERS_ID)
    public void deletePublisher(@PathVariable Integer id) {
        publisherService.delete(id);
    }
}
