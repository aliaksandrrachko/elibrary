package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.SubscriptionJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequestCode;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionScheduledTaskExecutorService;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.api.utils.mail.AdminMailMessageType;
import by.it.academy.grodno.elibrary.api.utils.mail.IEmailSender;
import by.it.academy.grodno.elibrary.api.utils.mail.UserMailMessageType;
import by.it.academy.grodno.elibrary.entities.books.Subscription;
import by.it.academy.grodno.elibrary.entitymetadata.books.SubscriptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class SubscriptionScheduledTaskExecutorService implements ISubscriptionScheduledTaskExecutorService {

    private static final String SUBSCRIPTION = "subscription";
    private static final String SUBSCRIPTIONS = "subscriptions";
    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final ISubscriptionService subscriptionService;
    private final IEmailSender emailSender;

    public SubscriptionScheduledTaskExecutorService(SubscriptionJpaRepository subscriptionJpaRepository,
                                                    ISubscriptionService subscriptionService, IEmailSender emailSender) {
        this.subscriptionJpaRepository = subscriptionJpaRepository;
        this.subscriptionService = subscriptionService;
        this.emailSender = emailSender;
    }

    @Override
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void findAllExpiredSubscriptionsAndSendEmailToUserAndAdmin() {
        Set<SubscriptionStatus> statuses = new HashSet<>();
        statuses.add(SubscriptionStatus.COMPLETED);
        statuses.add(SubscriptionStatus.BOOKING);
        Set<Subscription> subscriptions = subscriptionJpaRepository
                .findByDeadlineBeforeAndStatusNotIn(LocalDateTime.now().withNano(0), statuses);
        subscriptions.forEach(subscription -> {
            subscription.setStatus(SubscriptionStatus.READING_EXPIRED);
            subscriptionJpaRepository.save(subscription);
            emailSender.sendEmailFromAdmin(subscription.getUser(), UserMailMessageType.SUBSCRIPTION_EXPIRED,
                    Collections.singletonMap(SUBSCRIPTION, subscription));
        });
        if (!subscriptions.isEmpty()) {
            emailSender.sendEmailToAdmin(null, AdminMailMessageType.SUBSCRIPTION_EXPIRED_INFO,
                    Collections.singletonMap(SUBSCRIPTIONS, subscriptions));
        }
        log.info("Was execute scheduled task: 'change status to expired' count - {}", subscriptions.size());
    }

    @Override
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser() {
        Set<Subscription> subscriptions = subscriptionJpaRepository
                .findByDeadlineBeforeAndStatus(LocalDateTime.now().withNano(0), SubscriptionStatus.BOOKING);
        subscriptions.forEach(subscription -> {
            SubscriptionRequest request = new SubscriptionRequest(subscription.getId(),
                    subscription.getUser().getId(), SubscriptionRequestCode.UNDO_BOOKING.getRequestCode(),
                    0, subscription.getBook().getId(), subscription.getTook());
            subscriptionService.undoBooking(request);
            emailSender.sendEmailFromAdmin(subscription.getUser(), UserMailMessageType.BOOKING_WAS_UNDO,
                    Collections.singletonMap(SUBSCRIPTION, subscription));
        });
        log.info("Was execute scheduled task: 'undo expired booking' count - {}", subscriptions.size());
    }

    @Override
    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void findAllCompletedMonthAgoSubscriptionsAndDeleteIt() {
        Set<Subscription> subscriptions = subscriptionJpaRepository.findByDeadlineBeforeAndStatus(
                LocalDateTime.now().withNano(0).minusMonths(3), SubscriptionStatus.COMPLETED);
        subscriptions.forEach(subscriptionJpaRepository::delete);
    }
}
