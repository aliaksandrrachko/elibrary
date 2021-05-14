package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.BookDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.IBookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MainController {

    private final IUserService userService;
    private final IBookService bookService;

    public MainController(IUserService userService, IBookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/")
    public ModelAndView home(Principal principal) {
        UserDto userDto = null;
        if (principal != null) {
            String userName = principal.getName();
            userDto = userService.findById(userName);
        }
        List<BookDto> top6Books = bookService.findTop6ByRating();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("booksList", top6Books);
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/403")
    public ModelAndView error403() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("error", "You don't have permission");
        modelAndView.addObject("timestamp", LocalDateTime.now().withNano(0));
        return modelAndView;
    }
}
