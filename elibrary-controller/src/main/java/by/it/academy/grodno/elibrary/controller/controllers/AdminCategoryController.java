package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final IUserService userService;
    private final ICategoryService categoryService;
    public AdminCategoryController(IUserService userService, ICategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView getEditCategoryPage(Principal principal) {
        Set<CategoryDto> categoryDtoSet = new HashSet<>(categoryService.findAll());

        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("admin/adminCategoriesList");
        modelAndView.addObject("categoryDtoSet", categoryDtoSet);
        return modelAndView;
    }

    private static final String REDIRECT_TO_MAPPING_ADMIN_CATEGORIES = "redirect:/admin/categories";

    @PostMapping
    public ModelAndView createCategory(@Valid @ModelAttribute(value = "categoryDto") CategoryDto categoryDto,
                                       BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_CATEGORIES);
        if (result.hasErrors()) {
            modelAndView.addObject(result.getModel());
            modelAndView.addObject("categoryDto", categoryDto);
        } else {
            categoryService.create(categoryDto);
        }
        return modelAndView;
    }

    @PostMapping("/rename/{categoryId}")
    public ModelAndView renameCategory(@PathVariable @Valid @Min(0) int categoryId,
                                       @Valid @ModelAttribute(value = "categoryDto") CategoryDto categoryDto,
                                       BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_CATEGORIES);
        if (result.hasErrors()) {
            modelAndView.addObject(result.getModel());
            modelAndView.addObject("categoryDto", categoryDto);
        } else {
            categoryService.update(categoryId, categoryDto);
        }
        return modelAndView;
    }

    @PostMapping("/delete/{categoryId}")
    public ModelAndView deleteCategory(@PathVariable @Valid @Min(0) int categoryId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_CATEGORIES);
        categoryService.delete(categoryId);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithCurrentUserFromDb(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        UserDto currentUser = null;
        if (principal != null) {
            currentUser = userService.findById(principal.getName());
        }
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }
}
