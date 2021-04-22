package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final IUserService userService;

    public SignUpController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ModelAndView showSignUpForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("signup");
        modelAndView.addObject("userDto", new UserDto());
        return modelAndView;
    }

    @PostMapping()
    public ModelAndView signUp(@Valid @ModelAttribute(value = "userDto") UserDto userDto, BindingResult result) {
        ModelAndView modelAndView;
        try {
            if (result.hasErrors()) {
                modelAndView = new ModelAndView();
                modelAndView.setViewName("signup");
                modelAndView.addAllObjects(result.getModel());
            } else {
                userService.create(userDto);
                modelAndView = new ModelAndView();
                modelAndView.setViewName("redirect:/login");
            }
        } catch (Exception e) {
            modelAndView = new ModelAndView();
            modelAndView.addAllObjects(result.getModel());
            modelAndView.addObject("error",e.getMessage());
            modelAndView.setViewName("redirect:/signup");
        }
        return modelAndView;
    }
}
