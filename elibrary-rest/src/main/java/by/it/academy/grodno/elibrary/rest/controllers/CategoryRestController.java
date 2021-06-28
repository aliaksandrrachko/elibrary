package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Category.ADMIN_CATEGORIES;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Category.ADMIN_CATEGORIES_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Category.CATEGORIES;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Category.CATEGORIES_ID;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CategoryRestController {

    private final ICategoryService categoryService;

    public CategoryRestController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = CATEGORIES)
    public List<CategoryDto> findAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping(value = CATEGORIES_ID)
    public CategoryDto findCategory(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @PostMapping(value = ADMIN_CATEGORIES,consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PutMapping(value = ADMIN_CATEGORIES_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto dto, @PathVariable Integer id) {
        return categoryService.update(id, dto);
    }

    @DeleteMapping(value = ADMIN_CATEGORIES_ID)
    public void deleteCategory(@PathVariable Integer id) {
        categoryService.delete(id);
    }
}
