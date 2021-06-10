package by.it.academy.grodno.elibrary.rest.controllers;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/rest/subscriptions")
public class SubscriptionRestController {

    private final ISubscriptionService subscriptionService;

    public SubscriptionRestController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping
    public List<SubscriptionDto> findAllSubscription() {
        return subscriptionService.findAll();
    }

    @GetMapping(value = "/{id}")
    public SubscriptionDto findSubscription(@PathVariable Long id) {
        return subscriptionService.findById(id).orElse(null);
    }

    @GetMapping(value = "/subscriptions")
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.create(request).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto updateSubscription(@Valid @RequestBody SubscriptionRequest request, @PathVariable Long id) {
        return subscriptionService.update(id, request).orElse(null);
    }
}
