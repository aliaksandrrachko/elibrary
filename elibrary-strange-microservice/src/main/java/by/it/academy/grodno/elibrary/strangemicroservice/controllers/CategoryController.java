package by.it.academy.grodno.elibrary.strangemicroservice.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.strangemicroservice.services.EchoCategoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final EchoCategoryServiceImpl echoCategoryService;

    public CategoryController(EchoCategoryServiceImpl echoCategoryService) {
        this.echoCategoryService = echoCategoryService;
    }

    @GetMapping
    public Flux<CategoryDto> get() {
        return echoCategoryService.getCategoryAndSaveIntoMongo();
    }

    @GetMapping("/{id}")
    public Mono<CategoryDto> get(@PathVariable Long id) {
        return echoCategoryService.getCategoryAndSaveIntoMongo(id);
    }

}
