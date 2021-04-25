package by.it.academy.grodno.elibrary.controller.controllers;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.controller.utils.PageNumberListCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subscription")
public class SubscriptionController {

    private final IUserService userService;
    private final ISubscriptionService subscriptionService;

    public SubscriptionController(IUserService userService, ISubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ModelAndView findAll(@RequestParam(value = "status", required = false) Integer status,
                                Pageable pageable,
                                Principal principal){
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<SubscriptionDto> subscriptionPage =
                subscriptionService.findAllByUserIdAndStatus(userDto.getId(), status, pageable);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subscription/subscriptionInfo");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageSubscriptionDto", subscriptionPage);
        modelAndView.addObject("currentStatusCode", status);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(subscriptionPage.getNumber(), subscriptionPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping
    public ModelAndView bookingBook(SubscriptionRequest request,
                                    Principal principal){
        if (principal != null){
            Optional<UserDto> userDtoOptional = userService.findById(principal.getName());
            if (userDtoOptional.isPresent()) {
                request.setUserId(userDtoOptional.get().getId());
                subscriptionService.booking(request);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/books/" + request.getBookId());
        return modelAndView;
    }
}
