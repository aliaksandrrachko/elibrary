package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthorService extends IAGenericCrudService<AuthorDto, Integer> {
}
