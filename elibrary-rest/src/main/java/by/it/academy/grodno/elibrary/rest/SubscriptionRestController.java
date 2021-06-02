package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//@RestController
@RequestMapping(value = "rest/subscriptions")
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        return subscriptionService.create(request).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionDto updateSubscription(@Valid @RequestBody SubscriptionRequest request, @PathVariable Long id) {
        return subscriptionService.update(id, request).orElse(null);
    }
}
