package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
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
        Set<CategoryDto> categoryDtoSet = categoryService.findAllUnique();

        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("admin/adminCategoriesList");
        modelAndView.addObject("categoryDtoSet", categoryDtoSet);
        return modelAndView;
    }

    @PostMapping
    public ModelAndView createCategory(@Valid @ModelAttribute(value = "categoryDto") CategoryDto categoryDto,
                                       BindingResult result,
                                       Principal principal){
        ModelAndView modelAndView = getModelAndViewWithCurrentUserFromDb(principal);
        modelAndView.setViewName("redirect:/admin/categories");
        if (result.hasErrors()){
            modelAndView.addObject(result.getModel());
            modelAndView.addObject("categoryDto", categoryDto);
        } else {
            categoryService.create(categoryDto);
        }
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithCurrentUserFromDb(Principal principal){
        ModelAndView modelAndView = new ModelAndView();
        UserDto currentUser = null;
        if (principal != null){
            currentUser = userService.findById(principal.getName()).orElse(null);
        }
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }
}
