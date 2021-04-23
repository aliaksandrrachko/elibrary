package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("/info")
    public ModelAndView userInfo(Principal principal) {
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users/userInfo");
        modelAndView.addObject("currentUser", currentUser);
        modelAndView.addObject("userDto", currentUser);
        return modelAndView;
    }

    @GetMapping("/update")
    public ModelAndView getEditUserProfileForm(Principal principal) {
        UserDto currentUser = userService.findById(principal.getName()).orElse(null);

        ModelAndView modelAndView = new ModelAndView();
        if (currentUser == null) {
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "Authentication error!");
        } else {
            modelAndView.setViewName("users/userUpdate");
            modelAndView.addObject("currentUser", currentUser);
            modelAndView.addObject("userDto", currentUser);
        }
        return modelAndView;
    }

    @PostMapping("/update")
    public ModelAndView editUserProfile(@Valid @ModelAttribute(value = "userDto") UserDto userDto,
                                        BindingResult result,
                                        Principal principal) {
        ModelAndView modelAndView;
        if (principal == null) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("error");
            modelAndView.addObject("error", "You don't have permission");
        } else if (result.hasErrors()) {
            modelAndView = new ModelAndView();
            modelAndView.setViewName("users/userUpdate");
            modelAndView.addAllObjects(result.getModel());
            UserDto currentUser = userService.findById(principal.getName()).orElse(new UserDto());
            modelAndView.addObject("userDto", currentUser);
            modelAndView.addObject("currentUser", currentUser);
        } else {
            Optional<UserDto> optionalUpdatedUser = userService.update(Long.valueOf(principal.getName()), userDto);
            optionalUpdatedUser.ifPresent(this::changeUserInSecurityContext);
            modelAndView = new ModelAndView();
            modelAndView.setViewName("redirect:/users/info");
        }
        return modelAndView;
    }

    private void changeUserInSecurityContext(UserDto updatedUser){
        Collection<? extends GrantedAuthority> nowAuthorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        String.valueOf(updatedUser.getId()),
                        updatedUser.getPassword(),
                        nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
