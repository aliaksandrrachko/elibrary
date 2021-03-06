package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Collections;

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
                                    @PageableDefault(sort = {"username"}, direction = Sort.Direction.ASC) Pageable pageable) {
        UserDto userDto = userService.findUser(principal);

        Page<UserDto> pageUsersDto;
        UserDto foundUser;
        if (userId != null) {
            foundUser = userService.findById(userId);
        } else if (email != null) {
            foundUser = userService.findUserByEmail(email);
        } else {
            foundUser = null;
        }

        if (foundUser != null) {
            pageUsersDto = new PageImpl<>(Collections.singletonList(foundUser), pageable, 1L);
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

    private static final String REDIRECT_TO_MAPPING_ADMIN_USERS = "redirect:/admin/users";

    @PostMapping("/delete")
    public ModelAndView deleteUser(@Valid @Min(0) long userId) {
        userService.delete(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_USERS);
        return modelAndView;
    }

    @PostMapping("/roles/delete")
    public ModelAndView deleteUserRole(String roleName, @Valid @Min(0) long userId) {
        userService.deleteRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_USERS);
        return modelAndView;
    }

    @PostMapping("/roles/add")
    public ModelAndView addRoleToUser(String roleName, @Valid @Min(0) long userId) {
        userService.addRole(userId, roleName);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_USERS);
        return modelAndView;
    }

    @PostMapping("/setAvailability")
    public ModelAndView setUsersAvailability(@Valid @Min(0) long userId) {
        userService.setAvailability(userId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(REDIRECT_TO_MAPPING_ADMIN_USERS);
        return modelAndView;
    }
}
