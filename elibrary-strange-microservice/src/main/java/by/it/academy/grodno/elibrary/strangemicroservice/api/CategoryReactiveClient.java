package by.it.academy.grodno.elibrary.strangemicroservice.api;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Its reactive client don't work with simple controller
@ReactiveFeignClient(url = "http://localhost:8080", name = "main")
public interface CategoryReactiveClient {

    @GetMapping(value = "/rest/categories")
    Flux<CategoryDto> findAllCategories();

    @GetMapping(value = "/rest/categories/{id}", consumes = "application/json")
    Mono<CategoryDto> findCategory(@PathVariable("id") Long id);
}
