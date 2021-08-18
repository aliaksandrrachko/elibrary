package by.it.academy.grodno.elibrary.strangemicroservice.repositories;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoryReactiveMongoRepository extends ReactiveMongoRepository<CategoryDto, String> {
}
