package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.SubscriptionJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequestCode;
import by.it.academy.grodno.elibrary.api.mappers.SubscriptionMapper;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.api.utils.mail.AdminMailMessageType;
import by.it.academy.grodno.elibrary.api.utils.mail.IEmailSender;
import by.it.academy.grodno.elibrary.api.utils.mail.UserMailMessageType;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Subscription;
import by.it.academy.grodno.elibrary.entities.books.SubscriptionStatus;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.service.exceptions.UnknownSubscriptionUpdateCodeRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Component
@Slf4j
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookJpaRepository bookJpaRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final IEmailSender emailSender;

    public SubscriptionService(SubscriptionJpaRepository subscriptionJpaRepository,
                               BookJpaRepository bookJpaRepository,
                               SubscriptionMapper subscriptionMapper,
                               UserJpaRepository userJpaRepository, IEmailSender emailSender) {
        this.subscriptionJpaRepository = subscriptionJpaRepository;
        this.bookJpaRepository = bookJpaRepository;
        this.subscriptionMapper = subscriptionMapper;
        this.userJpaRepository = userJpaRepository;
        this.emailSender = emailSender;
    }

    @Override
    public List<SubscriptionDto> findAll() {
        return subscriptionMapper.toDtos(subscriptionJpaRepository.findAll());
    }

    @Override
    public Page<SubscriptionDto> findAll(Pageable pageable) {
        return subscriptionMapper.toPageDto( subscriptionJpaRepository.findAll(pageable));
    }

    @Override
    public Page<SubscriptionDto> findAllByUserIdAndStatus(@NotNull Long userId, @NotNull Integer statusCode, Pageable pageable) {
        SubscriptionStatus status = SubscriptionStatus.getSubscriptionStatus(statusCode);
        Page<Subscription> subscriptionPage;
        if (status.equals(SubscriptionStatus.READING_EXPIRED)){
            subscriptionPage = subscriptionJpaRepository
                    .findByUserIdAndDeadlineBeforeAndStatusNot(userId, LocalDateTime.now().withNano(0),
                            SubscriptionStatus.COMPLETED, pageable);
        } else {
            subscriptionPage = subscriptionJpaRepository.findAllByUserIdAndStatusIn(userId, Collections.singleton(status), pageable);
        }
        return subscriptionMapper.toPageDto(subscriptionPage);
    }

    @Override
    public Page<SubscriptionDto> findAllByUserId(Long userId, Pageable pageable){
        return subscriptionMapper.toPageDto(subscriptionJpaRepository.findByUserId(userId, pageable));
    }

    @Override
    public Page<SubscriptionDto> findAllByStatus(@NotNull Integer statusCode, Pageable pageable){
        SubscriptionStatus status = SubscriptionStatus.getSubscriptionStatus(statusCode);
        Page<Subscription> subscriptionPage;
        if (status.equals(SubscriptionStatus.READING_EXPIRED)){
            subscriptionPage = subscriptionJpaRepository
                    .findByDeadlineBeforeAndStatusNot(LocalDateTime.now().withNano(0),
                            SubscriptionStatus.COMPLETED, pageable);
        } else {
            subscriptionPage = subscriptionJpaRepository.findAllByStatusIn(Collections.singleton(status), pageable);
        }
        return subscriptionMapper.toPageDto(subscriptionPage);
    }

    @Override
    public Optional<SubscriptionDto> findById(Long id) {
        return subscriptionJpaRepository.findById(id).map(subscriptionMapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Subscription> subscriptionOptional = subscriptionJpaRepository.findById(id);
        if (subscriptionOptional.isPresent() && subscriptionOptional.get().getStatus().equals(SubscriptionStatus.COMPLETED)) {
            subscriptionOptional.ifPresent(subscriptionJpaRepository::delete);
        }
    }

    private static final int DEFAULT_BOOKING_BOOK_COUNT = 1;
    private static final int DEFAULT_BOOKING_BOOK_DAYS = 1;

    static final String SUBSCRIPTION = "subscription";
    static final String SUBSCRIPTIONS = "subscriptions";

    @Override
    @Transactional
    public Optional<SubscriptionDto> booking(SubscriptionRequest request) {
        request.setCount(DEFAULT_BOOKING_BOOK_COUNT);
        request.setDays(DEFAULT_BOOKING_BOOK_DAYS);
        request.setCode(1);
        Optional<Subscription> subscriptionOptional = createAndSave(request);
        if (subscriptionOptional.isPresent()){
            Subscription subscription = subscriptionOptional.get();
            emailSender.sendEmailFromAdmin(subscription.getUser(), UserMailMessageType.USER_BOOKING_BOOK,
                    Collections.singletonMap(SUBSCRIPTION, subscription));
        }
        return subscriptionOptional.map(subscriptionMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<SubscriptionDto> undoBooking(SubscriptionRequest request) {
        request.setCode(5);
        return update(request.getId(), request);
    }

    @Override
    public Optional<SubscriptionDto> findBySubscriptionIdAndUserId(Long subscriptionId, Long id) {
        return subscriptionJpaRepository.findByIdAndUserId(subscriptionId, id).map(subscriptionMapper::toDto);
    }

    @Override
    @Transactional
    public Optional<SubscriptionDto> create(SubscriptionRequest request) {
        return createAndSave(request).map(subscriptionMapper::toDto);
    }

    private Optional<Subscription> createAndSave(SubscriptionRequest request) {
        Optional<Book> optionalBook = bookJpaRepository.findById(request.getBookId());
        Optional<User> optionalUser = userJpaRepository.findById(request.getUserId());

        SubscriptionRequestCode code = SubscriptionRequestCode.getSubscriptionRequestCode(request.getCode());
        SubscriptionStatus status;
        if (code.equals(SubscriptionRequestCode.TAKE_BOOK)){
            status = SubscriptionStatus.READING;
        } else {
            status = SubscriptionStatus.BOOKING;
        }

        if (optionalBook.isPresent() && optionalUser.isPresent()) {
            Book book = optionalBook.get();
            User user = optionalUser.get();
            if (book.isAvailable()) {
                int amountTaken = takeBook(book, request.getCount());
                Subscription subscription = Subscription.builder()
                        .status(status)
                        .created(LocalDateTime.now().withNano(0))
                        .deadline(LocalDateTime.now().withNano(0).plusDays(request.getDays()))
                        .returned(0)
                        .book(book)
                        .took(amountTaken)
                        .user(user)
                        .build();
                subscription = subscriptionJpaRepository.save(subscription);
                return Optional.of(subscription);
            }
        }
        return Optional.empty();
    }

    private int takeBook(@NotNull Book book, int count) {
        int taken;
        if (book.getAvailableCount() >= count) {
            book.setAvailableCount(book.getAvailableCount() - count);
            taken = count;
        } else {
            taken = book.getAvailableCount();
            book.setAvailableCount(0);
        }
        return taken;
    }

    @Override
    @Transactional
    public Optional<SubscriptionDto> update(Long id, SubscriptionRequest request) {
        Optional<Subscription> optionalSubscription = subscriptionJpaRepository.findById(id);
        if (!optionalSubscription.isPresent()) {
            return Optional.empty();
        }
        Subscription subscription = optionalSubscription.get();
        SubscriptionRequestCode requestCode = SubscriptionRequestCode.getSubscriptionRequestCode(request.getCode());

        switch (requestCode) {
            case TAKE_BOOK:
                prepareToTakeBookSubscription(subscription, request);
                break;
            case LEAVE_BOOK:
                prepareToLeaveBookSubscription(subscription, request);
                break;
            case EXTENDED_SUBSCRIPTION:
                prepareToExtendedSubscription(subscription, request);
                break;
            case UNDO_BOOKING:
                prepareToUndoBookingSubscription(subscription, request);
                break;
            default:
                throw new UnknownSubscriptionUpdateCodeRequest(requestCode);
        }
        return Optional.of(subscriptionMapper.toDto(subscriptionJpaRepository.save(subscription)));
    }

    private void prepareToUndoBookingSubscription(Subscription subscription, SubscriptionRequest request) {
        int debtBook = subscription.getTook() - subscription.getReturned();
        int returnsBook = request.getCount();
        if (debtBook >= returnsBook && (subscription.getStatus().equals(SubscriptionStatus.BOOKING))){
            leaveBook(subscription.getBook(), returnsBook);
            subscription.setReturned(subscription.getReturned() + returnsBook);
            if (debtBook == returnsBook) {
                subscription.setStatus(SubscriptionStatus.COMPLETED);
                subscription.setDeadline(LocalDateTime.now().withNano(0));
            } else {
                subscription.setStatus(SubscriptionStatus.BOOKING);
            }
        }
    }

    private void prepareToExtendedSubscription(Subscription subscription, SubscriptionRequest request) {
        if (request.getDays() > 0) {
            subscription.setDeadline(subscription.getDeadline().plusDays(request.getDays()));
            subscription.setStatus(SubscriptionStatus.READING_EXTENDED);
        }
    }

    private void prepareToLeaveBookSubscription(Subscription subscription, SubscriptionRequest request) {
        int debtBook = subscription.getTook() - subscription.getReturned();
        int returnsBook = request.getCount();
        if (debtBook >= returnsBook && (subscription.getStatus().equals(SubscriptionStatus.READING) ||
                subscription.getStatus().equals(SubscriptionStatus.READING_EXTENDED))) {
            leaveBook(subscription.getBook(), returnsBook);
            subscription.setReturned(subscription.getReturned() + returnsBook);
            if (debtBook == returnsBook) {
                subscription.setStatus(SubscriptionStatus.COMPLETED);
                subscription.setDeadline(LocalDateTime.now().withNano(0));
                subscription.getBook().setRating(subscription.getBook().getRating() + 1);
            } else {
                subscription.setStatus(SubscriptionStatus.READING_EXTENDED);
            }
        }
    }

    private void prepareToTakeBookSubscription(Subscription subscription, SubscriptionRequest request) {
        if (subscription.getStatus().equals(SubscriptionStatus.BOOKING) && subscription.getTook() >= request.getCount()) {
            Book book = subscription.getBook();
            int remainder = subscription.getTook() - request.getCount();
            leaveBook(book, remainder);
            subscription.setTook(request.getCount());
            subscription.setReturned(0);
            subscription.setStatus(SubscriptionStatus.READING);
            subscription.setCreated(LocalDateTime.now().withNano(0));
            subscription.setDeadline(LocalDateTime.now().withNano(0).plusDays(request.getDays()));
        }
    }

    private void leaveBook(Book book, int count) {
        book.setAvailableCount(book.getAvailableCount() + count);
    }

    @Scheduled(cron = "0 * * * * *")
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

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void findAllExpiredBookingSubscriptionAndUndoBookingAndBeforeSendEmailToUser() {
        Set<Subscription> subscriptions = subscriptionJpaRepository
                .findByDeadlineBeforeAndStatus(LocalDateTime.now().withNano(0), SubscriptionStatus.BOOKING);
        subscriptions.forEach(subscription -> {
            SubscriptionRequest request = new SubscriptionRequest(subscription.getId(),
                    subscription.getUser().getId(), SubscriptionRequestCode.UNDO_BOOKING.getRequestCode(),
                    0, subscription.getBook().getId(), subscription.getTook());
            undoBooking(request);
            emailSender.sendEmailFromAdmin(subscription.getUser(), UserMailMessageType.BOOKING_WAS_UNDO,
                    Collections.singletonMap(SUBSCRIPTION, subscription));
        });
        log.info("Was execute scheduled task: 'undo expired booking' count - {}", subscriptions.size());
    }
}
