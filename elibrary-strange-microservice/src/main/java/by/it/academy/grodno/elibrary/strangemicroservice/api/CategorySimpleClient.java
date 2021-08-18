package by.it.academy.grodno.elibrary.strangemicroservice.api;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "http://localhost:8080", name = "main")
public interface CategorySimpleClient {

    @GetMapping(value = "/rest/categories")
    List<CategoryDto> findAllCategories();

    @GetMapping(value = "/rest/categories/{id}", consumes = "application/json")
    CategoryDto findCategory(@PathVariable("id") Long id);
}
