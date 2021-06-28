package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.Subscription.ADMIN_SUBSCRIPTIONS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Subscription.ADMIN_SUBSCRIPTIONS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Subscription.SUBSCRIPTIONS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Subscription.SUBSCRIPTIONS_ID;
import static by.it.academy.grodno.elibrary.api.constants.Routes.Subscription.SUBSCRIPTIONS_STATUS;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@RestController
public class SubscriptionRestController {

    private final ISubscriptionService subscriptionService;

    public SubscriptionRestController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = SUBSCRIPTIONS_STATUS)
    public Page<SubscriptionDto> findAllSubscriptionsByStatusForCurrentUser(@PathVariable Integer status,
                                                                            @PageableDefault(sort = {"created"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                            Principal principal){
        if (principal == null){
            return null;
        } else {
            return subscriptionService.findAllByUserIdAndStatus(Long.valueOf(principal.getName()), status, pageable);
        }
    }

    @GetMapping(value = SUBSCRIPTIONS_ID)
    public Page<SubscriptionDto> findAllSubscriptionByIdForCurrentUser(@PathVariable Long id,
                                                                       @PageableDefault(sort = {"status"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                       Principal principal){
        Optional<SubscriptionDto> optionalSubscriptionDto =  subscriptionService.findBySubscriptionIdAndUserId(id, Long.valueOf(principal.getName()));
        if (optionalSubscriptionDto.isPresent()){
            return new PageImpl<>(Collections.singletonList(optionalSubscriptionDto.get()), pageable, 1);
        }else {
            return Page.empty(pageable);
        }
    }

    @GetMapping(value = SUBSCRIPTIONS)
    public Page<SubscriptionDto> findAllSubscriptionForCurrentUser(@PageableDefault(sort = {"status"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                   Principal principal){
        if (principal == null){
            return null;
        } else {
               return subscriptionService.findAllByUserId(Long.valueOf(principal.getName()), pageable);
        }
    }

    @PostMapping(value = SUBSCRIPTIONS)
    public SubscriptionDto createSubscription(@Valid @RequestBody SubscriptionRequest request, Principal principal){
        request.setUserId(Long.parseLong(principal.getName()));
        return subscriptionService.booking(request).orElse(null);
    }

    @DeleteMapping(value = SUBSCRIPTIONS)
    public void undoSubscription(@Valid @RequestBody SubscriptionRequest request, Principal principal){
        request.setUserId(Long.parseLong(principal.getName()));
        subscriptionService.undoBooking(request);
    }

    @GetMapping(value = ADMIN_SUBSCRIPTIONS)
    public Page<SubscriptionDto> findAllSubscription(@RequestParam(value = "status", required = false) Integer status,
                                                     @RequestParam(value = "userId", required = false) Long userId,
                                                     @RequestParam(value = "subscriptionId", required = false) Long subscriptionId,
                                                     @PageableDefault(sort = {"status"}, direction = Sort.Direction.ASC) Pageable pageable) {
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
        return subscriptionPage;
    }

    @GetMapping(value = ADMIN_SUBSCRIPTIONS_ID)
    public SubscriptionDto findSubscription(@PathVariable Long id) {
        return subscriptionService.findById(id).orElse(null);
    }

    @PostMapping(value = ADMIN_SUBSCRIPTIONS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.create(request).orElse(null);
    }

    @PutMapping(value = ADMIN_SUBSCRIPTIONS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto updateSubscription(@Valid @RequestBody SubscriptionRequest request, @PathVariable Long id) {
        return subscriptionService.update(id, request).orElse(null);
    }
}
