package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.books.CategoryDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IUserService userService;
    private final ISubscriptionService subscriptionService;

    public AdminController(IUserService userService, ISubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/subscriptions")
    public ModelAndView findAllSubscription(@RequestParam(value = "status", required = false, defaultValue = "0") Integer status,
                                            @RequestParam(value = "userId", required = false) Long userId,
                                            @RequestParam(value = "subscriptionId", required = false) Long subscriptionId,
                                            Pageable pageable,
                                            Principal principal) {
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<SubscriptionDto> subscriptionPage;
        Optional<SubscriptionDto> optionalSubscriptionDto;
        if (subscriptionId != null && (optionalSubscriptionDto = subscriptionService.findById(subscriptionId)).isPresent()) {
            subscriptionPage = new PageImpl<>(Collections.singletonList(optionalSubscriptionDto.get()));
        } else {
            subscriptionPage = subscriptionService.findAllByUserIdAndStatus(userId, status, pageable);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminSubscriptionList");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageSubscriptionDto", subscriptionPage);
        modelAndView.addObject("currentStatusCode", status);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(subscriptionPage.getNumber(), subscriptionPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping(value = "/subscriptions")
    public ModelAndView updateSubscription(@Valid SubscriptionRequest request,
                                           BindingResult result) {
        if (!result.hasErrors()) {
            subscriptionService.update(request.getId(), request);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/subscriptions");
        return modelAndView;
    }

    @PostMapping(value = "/subscriptions/create")
    public ModelAndView createSubscription(@Valid SubscriptionRequest request,
                                           BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        Optional<SubscriptionDto> optionalSubscriptionDto;
        String redirectUrl = "redirect:/admin/subscriptions";
        if (!result.hasErrors() && (optionalSubscriptionDto = subscriptionService.create(request)).isPresent()){
            redirectUrl += "?subscriptionId=" + optionalSubscriptionDto.get().getId();
        }
        modelAndView.setViewName(redirectUrl);
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView findAllUser(@RequestParam(value = "userId", required = false) Long userId,
                                    @RequestParam(value = "email", required = false) String email,
                                    Principal principal,
                                    @PageableDefault Pageable pageable) {
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<UserDto> pageUsersDto;
        Optional<UserDto> foundUser;
        if (userId != null){
            foundUser = userService.findById(userId);
        } else if (email != null){
            foundUser = userService.findUserByEmail(email);
        } else {
            foundUser = Optional.empty();
        }

        if (foundUser.isPresent()) {
            pageUsersDto = new PageImpl<>(Collections.singletonList(foundUser.get()));
        } else {
            pageUsersDto = userService.findAll(pageable);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/usersList");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageUsersDto", pageUsersDto);
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
