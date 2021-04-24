package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.services.IAGenericCrudService;

import java.util.Optional;

public interface ICategoryService extends IAGenericCrudService<CategoryDto, Integer> {

    Optional<CategoryDto> findByCategoryName(String categoryName);
}
