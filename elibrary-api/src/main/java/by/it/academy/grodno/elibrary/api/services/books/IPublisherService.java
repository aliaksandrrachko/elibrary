package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPublisherService extends IAGenericCrudService<PublisherDto, Integer> {
    Page<PublisherDto> findAllPublisherWithBookByPublisherNameLike(String publisherName, Pageable pageable);
}
