package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import by.it.academy.grodno.elibrary.api.services.books.ICategoryService;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private ISubscriptionService subscriptionService;

    @GetMapping(value = "/subscriptions")
    public ModelAndView findAllSubscription(@RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                                            @RequestParam(value = "userId", required = false) Long userId,
                                            Pageable pageable,
                                            Principal principal) {
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<SubscriptionDto> subscriptionPage =
                subscriptionService.findAllByUserIdAndStatus(userId, status, pageable);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/subscriptionUpdate");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageSubscriptionDto", subscriptionPage);
        modelAndView.addObject("currentStatusCode", status);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(subscriptionPage.getNumber(), subscriptionPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping(value = "/subscriptions")
    public ModelAndView updateSubscription(@Valid @RequestBody SubscriptionRequest request,
                                           BindingResult result) {
        if (!result.hasErrors()) {
            subscriptionService.update(request.getId(), request);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/subscriptions");
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView findAllUser(Principal principal,
                                    @PageableDefault Pageable pageable) {
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<UserDto> pageUsersDto = userService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/usersList");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageUsersDto", pageUsersDto);
//      modelAndView.addObject("roleDtoList", roleService.findAll());
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageUsersDto.getNumber(), pageUsersDto.getTotalPages()));
        return modelAndView;
    }

    @PostMapping("users/delete")
    public ModelAndView deleteUser(@Valid @Min(0) long userId) {
        userService.delete(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("users/roles/delete")
    public ModelAndView deleteUserRole(String roleName, @Valid @Min(0) long userId) {
        userService.deleteRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("users/roles/add")
    public ModelAndView addRoleToUser(String roleName, @Valid @Min(0) long userId) {
        userService.addRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("users/setAvailability")
    public ModelAndView setUsersAvailability(@Valid @Min(0) long userId) {
        userService.setAvailability(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }
}
