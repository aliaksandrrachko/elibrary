package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/admin/users")
public class AdminUserController {

    private final IUserService userService;

    public AdminUserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
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
        modelAndView.setViewName("admin/adminUsersList");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageUsersDto", pageUsersDto);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(pageUsersDto.getNumber(), pageUsersDto.getTotalPages()));
        return modelAndView;
    }

    @PostMapping("/delete")
    public ModelAndView deleteUser(@Valid @Min(0) long userId) {
        userService.delete(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("/roles/delete")
    public ModelAndView deleteUserRole(String roleName, @Valid @Min(0) long userId) {
        userService.deleteRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("/roles/add")
    public ModelAndView addRoleToUser(String roleName, @Valid @Min(0) long userId) {
        userService.addRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }

    @PostMapping("/setAvailability")
    public ModelAndView setUsersAvailability(@Valid @Min(0) long userId) {
        userService.setAvailability(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");
        return modelAndView;
    }
}
