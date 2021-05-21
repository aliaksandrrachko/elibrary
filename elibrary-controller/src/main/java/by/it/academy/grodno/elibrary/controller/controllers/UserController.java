package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.controller.constants.Template;
import by.it.academy.grodno.elibrary.entities.users.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "users")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    private static final String MODEL_ATTRIBUTE_NAME_CURRENT_USER = "currentUser";
    private static final String MODEL_ATTRIBUTE_NAME_USER_DTO = "userDto";

    @GetMapping("/info")
    public ModelAndView userInfo(Principal principal) {
        UserDto currentUser = this.userService.findById(principal.getName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/userInfo");
        modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, currentUser);
        modelAndView.addObject(MODEL_ATTRIBUTE_NAME_USER_DTO, currentUser);
        return modelAndView;
    }

    @GetMapping("/update")
    public ModelAndView getEditUserProfileForm(Principal principal) {
        UserDto currentUser = this.userService.findById(principal.getName());

        ModelAndView modelAndView = new ModelAndView();
        if (currentUser == null) {
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.ERROR_TEXT, "Authentication error!");
        } else {
            modelAndView.setViewName("users/userUpdate");
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, currentUser);
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_USER_DTO, currentUser);
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView editUserProfile(@Valid @ModelAttribute(value = "userDto") UserDto userDto,
                                        BindingResult result,
                                        @RequestParam(value = "fileAvatar", required = false) MultipartFile file,
                                        Principal principal) {
        ModelAndView modelAndView;
        if (principal == null) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName(Template.Error.TEMPLATE_NAME);
            modelAndView.addObject(Template.Error.ERROR_TEXT, "You don't have permission");
        } else if (result.hasErrors()) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("users/userUpdate");
            modelAndView.addAllObjects(result.getModel());
            UserDto currentUser = this.userService.findById(principal.getName());
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_USER_DTO, currentUser);
            modelAndView.addObject(MODEL_ATTRIBUTE_NAME_CURRENT_USER, currentUser);
        } else {
            Optional<UserDto> optionalUpdatedUser = this.userService.update(Long.valueOf(principal.getName()), userDto, file);
            optionalUpdatedUser.ifPresent(this::changeUserInSecurityContext);
            modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/users/info");
        }
        return modelAndView;
    }

    private void changeUserInSecurityContext(UserDto updatedUser) {
        Collection<? extends GrantedAuthority> nowAuthorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        Set<SimpleGrantedAuthority> newAuthorities = new HashSet<>();
        nowAuthorities.forEach(grantedAuthority -> newAuthorities.add(new SimpleGrantedAuthority(grantedAuthority.getAuthority())));
        updatedUser.getRoles().forEach(role -> newAuthorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        String.valueOf(updatedUser.getId()),
                        updatedUser.getPassword(),
                        newAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
