package by.it.academy.grodno.elibrary.strangemicroservice.services;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.strangemicroservice.api.CategorySimpleClient;
import by.it.academy.grodno.elibrary.strangemicroservice.api.EchoCategoryService;
import by.it.academy.grodno.elibrary.strangemicroservice.repositories.CategoryReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EchoCategoryServiceImpl implements EchoCategoryService {

    private final CategorySimpleClient categoryReactiveClient;
    private final CategoryReactiveMongoRepository categoryReactiveMongoRepository;

    public EchoCategoryServiceImpl(CategorySimpleClient categoryReactiveClient,
                                   CategoryReactiveMongoRepository categoryReactiveMongoRepository) {
        this.categoryReactiveClient = categoryReactiveClient;
        this.categoryReactiveMongoRepository = categoryReactiveMongoRepository;
    }

    @Override
    public Flux<CategoryDto> getCategoryAndSaveIntoMongo() {
        return categoryReactiveMongoRepository.saveAll(categoryReactiveClient.findAllCategories());
    }

    @Override
    public Mono<CategoryDto> getCategoryAndSaveIntoMongo(Long id) {
        return categoryReactiveMongoRepository.save(categoryReactiveClient.findCategory(id));
    }
}
