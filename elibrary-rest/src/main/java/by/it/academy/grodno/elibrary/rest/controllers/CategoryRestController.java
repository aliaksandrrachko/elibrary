package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/categories")
public class CategoryRestController {

    private final ICategoryService categoryService;

    public CategoryRestController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public List<CategoryDto> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping(value = "/{id}")
    public CategoryDto findCategory(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto dto, @PathVariable Integer id) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
