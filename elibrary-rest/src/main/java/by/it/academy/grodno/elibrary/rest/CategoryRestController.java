package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "rest/categories")
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
        return categoryService.findById(id).orElse(null);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        return categoryService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto updateCategory(@RequestBody CategoryDto dto, @PathVariable Integer id) {
        return categoryService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
