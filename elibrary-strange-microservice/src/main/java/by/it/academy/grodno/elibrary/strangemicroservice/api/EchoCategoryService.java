package by.it.academy.grodno.elibrary.strangemicroservice.api;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EchoCategoryService {

    Flux<CategoryDto> getCategoryAndSaveIntoMongo();
    Mono<CategoryDto> getCategoryAndSaveIntoMongo(Long id);
}
