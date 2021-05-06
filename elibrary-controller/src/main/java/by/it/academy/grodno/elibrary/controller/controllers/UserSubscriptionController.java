package by.it.academy.grodno.elibrary.controller.controllers;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping(value = "/subscriptions")
public class UserSubscriptionController {

    private final IUserService userService;
    private final ISubscriptionService subscriptionService;

    public UserSubscriptionController(IUserService userService, ISubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public ModelAndView findAll(@RequestParam(value = "status", required = false) @Min(1) @Max(5) Integer status,
                                @RequestParam(value = "subscriptionId", required = false) Long subscriptionId,
                                @PageableDefault Pageable pageable,
                                Principal principal){
        Optional<UserDto> optionalUserDto = userService.findUser(principal);
        UserDto userDto = optionalUserDto.orElseThrow(NoSuchElementException::new);

        Page<SubscriptionDto> subscriptionPage;
        if (status != null){
            subscriptionPage = subscriptionService.findAllByUserIdAndStatus(userDto.getId(), status, pageable);
        } else if(subscriptionId != null) {
            Optional<SubscriptionDto> optionalSubscriptionDto =
                    subscriptionService.findBySubscriptionIdAndUserId(subscriptionId, userDto.getId());
            subscriptionPage = new PageImpl<>(Collections.singletonList(optionalSubscriptionDto.orElse(null)));
        } else {
            subscriptionPage = subscriptionService.findAllByUserId(userDto.getId(), pageable);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("subscriptions/subscriptionInfo");
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
        modelAndView.setViewName("redirect:/subscriptions");
        return modelAndView;
    }

    @PostMapping(value = "/undo")
    public ModelAndView undoBookingBook(SubscriptionRequest request,
                                    Principal principal){
        if (principal != null){
            Optional<UserDto> userDtoOptional = userService.findById(principal.getName());
            if (userDtoOptional.isPresent()) {
                request.setUserId(userDtoOptional.get().getId());
                subscriptionService.undoBooking(request);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/subscriptions");
        return modelAndView;
    }
}
