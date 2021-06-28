package by.it.academy.grodno.elibrary.rest.controllers;

import static by.it.academy.grodno.elibrary.api.constants.Routes.ScheduledTask.SCHEDULED_TASK_DELETE_COMPLETED_MONT_AGO_SUBSCRIPTIONS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.ScheduledTask.SCHEDULED_TASK_UNDO_EXPIRED_SUBSCRIPTIONS;
import static by.it.academy.grodno.elibrary.api.constants.Routes.ScheduledTask.SCHEDULED_TASK_WARN_ABOUT_EXPIRATION_PERIOD;

import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionScheduledTaskExecutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ScheduledTaskSubscriptionController {

    private final ISubscriptionScheduledTaskExecutorService subscriptionScheduledTaskExecutorService;


    public ScheduledTaskSubscriptionController(ISubscriptionScheduledTaskExecutorService subscriptionScheduledTaskExecutorService) {
        this.subscriptionScheduledTaskExecutorService = subscriptionScheduledTaskExecutorService;
    }

    @PostMapping(value = SCHEDULED_TASK_UNDO_EXPIRED_SUBSCRIPTIONS)
    public ResponseEntity<String> undoAllExpiredSubscriptionsAndSendEmails() {
        subscriptionScheduledTaskExecutorService.
                findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = SCHEDULED_TASK_WARN_ABOUT_EXPIRATION_PERIOD)
    public ResponseEntity<String> findAllExpiredSubscriptingAndSendEmails() {
        subscriptionScheduledTaskExecutorService.findAllExpiredSubscriptionsAndSendEmailToUserAndAdmin();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = SCHEDULED_TASK_DELETE_COMPLETED_MONT_AGO_SUBSCRIPTIONS)
    public ResponseEntity<String> findAndDeleteCompletedMonthAgoSubscriptions() {
        subscriptionScheduledTaskExecutorService.findAllCompletedMonthAgoSubscriptionsAndDeleteIt();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
