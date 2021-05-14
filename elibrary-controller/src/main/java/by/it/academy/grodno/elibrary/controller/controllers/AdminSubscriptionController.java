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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminSubscriptionController {

    private final IUserService userService;
    private final ISubscriptionService subscriptionService;

    public AdminSubscriptionController(IUserService userService, ISubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/subscriptions")
    public ModelAndView findAllSubscription(@RequestParam(value = "status", required = false) Integer status,
                                            @RequestParam(value = "userId", required = false) Long userId,
                                            @RequestParam(value = "subscriptionId", required = false) Long subscriptionId,
                                            @PageableDefault(sort = {"status"}, direction = Sort.Direction.ASC) Pageable pageable,
                                            Principal principal) {
        UserDto userDto = userService.findUser(principal);

        Page<SubscriptionDto> subscriptionPage;
        Optional<SubscriptionDto> optionalSubscriptionDto;
        if (status != null) {
            subscriptionPage = subscriptionService.findAllByStatus(status, pageable);
        } else if (userId != null){
            subscriptionPage = subscriptionService.findAllByUserId(userId, pageable);
        } else if (subscriptionId != null) {
            optionalSubscriptionDto = subscriptionService.findById(subscriptionId);
            if (optionalSubscriptionDto.isPresent()){
                subscriptionPage = new PageImpl<>(Collections.singletonList(optionalSubscriptionDto.get()));
            } else {
                subscriptionPage = Page.empty(pageable);
            }
        } else {
            subscriptionPage = subscriptionService.findAll(pageable);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/adminSubscriptionList");
        modelAndView.addObject("currentUser", userDto);
        modelAndView.addObject("pageSubscriptionDto", subscriptionPage);
        modelAndView.addObject("currentStatusCode", status);
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("pageNumbers",
                PageNumberListCreator.getListOfPagesNumber(subscriptionPage.getNumber(), subscriptionPage.getTotalPages()));
        return modelAndView;
    }

    @PostMapping(value = "/subscriptions/update")
    public ModelAndView updateSubscription(@Valid SubscriptionRequest request,
                                           BindingResult result) {
        if (!result.hasErrors()) {
            subscriptionService.update(request.getId(), request);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/subscriptions");
        return modelAndView;
    }

    @PostMapping(value = "/subscriptions")
    public ModelAndView createSubscription(@Valid SubscriptionRequest request,
                                           BindingResult result){
        ModelAndView modelAndView = new ModelAndView();
        Optional<SubscriptionDto> optionalSubscriptionDto;
        String redirectUrl = "redirect:/admin/subscriptions";
        if (!result.hasErrors()){
            optionalSubscriptionDto = subscriptionService.create(request);
            if (optionalSubscriptionDto.isPresent()) {
                redirectUrl += "?subscriptionId=" + optionalSubscriptionDto.get().getId();
            }
        }
        modelAndView.setViewName(redirectUrl);
        return modelAndView;
    }
}
