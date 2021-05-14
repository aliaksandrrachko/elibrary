package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionScheduledTaskExecutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "rest/subscriptions/scheduledtask")
public class ScheduledTaskSubscriptionController {

    private final ISubscriptionScheduledTaskExecutorService subscriptionScheduledTaskExecutorService;


    public ScheduledTaskSubscriptionController(ISubscriptionScheduledTaskExecutorService subscriptionScheduledTaskExecutorService) {
        this.subscriptionScheduledTaskExecutorService = subscriptionScheduledTaskExecutorService;
    }

    @PostMapping(value = "undoExpiredSubscriptions")
    public ResponseEntity<String> undoAllExpiredSubscriptionsAndSendEmails(){
        subscriptionScheduledTaskExecutorService.
                findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "warnAboutExpirationPeriod")
    public ResponseEntity<String> findAllExpiredSubscriptingAndSendEmails(){
        subscriptionScheduledTaskExecutorService.findAllExpiredSubscriptionsAndSendEmailToUserAndAdmin();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "deleteCompletedMontAgoSubscriptions")
    public ResponseEntity<String> findAndDeleteCompletedMonthAgoSubscriptions(){
        subscriptionScheduledTaskExecutorService.findAllCompletedMonthAgoSubscriptionsAndDeleteIt();
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
