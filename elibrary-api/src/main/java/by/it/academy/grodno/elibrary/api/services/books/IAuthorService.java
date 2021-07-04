package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;

import java.util.List;


public interface IAuthorService extends IAGenericCrudService<AuthorDto, Integer> {

    List<AuthorDto> findWhoHasTheMostBooks();
}
