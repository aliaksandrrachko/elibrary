package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.SubscriptionJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.mappers.SubscriptionMapper;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionService;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Subscription;
import by.it.academy.grodno.elibrary.entities.books.SubscriptionStatus;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SubscriptionService implements ISubscriptionService {

    @Autowired
    private SubscriptionJpaRepository subscriptionJpaRepository;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private BookJpaRepository bookJpaRepository;

    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Override
    public List<SubscriptionDto> findAll() {
        return subscriptionMapper.toDtos(subscriptionJpaRepository.findAll());
    }

    @Override
    public Optional<SubscriptionDto> findById(Long id) {
        return subscriptionJpaRepository.findById(id).map(s -> subscriptionMapper.toDto(s));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<Subscription> bookOptional = subscriptionJpaRepository.findById(id);
        if (bookOptional.isPresent() && bookOptional.get().getStatus().equals(SubscriptionStatus.COMPLETED)){
            bookOptional.ifPresent(book -> subscriptionJpaRepository.delete(book));
        }
    }

    private static final int DEFAULT_BOOKING_COUNT = 1;
    private static final int DEFAULT_BOOKING_DAYS = 1;

    @Override
    public Optional<SubscriptionDto> booking(SubscriptionRequest entityDto){
        entityDto.setCount(DEFAULT_BOOKING_COUNT);
        entityDto.setDays(DEFAULT_BOOKING_DAYS);
        entityDto.setStatus(1);
        return createAndSave(entityDto);
    }

    @Override
    @Transactional
    public Optional<SubscriptionDto> create(SubscriptionRequest entityDto) {
        return createAndSave(entityDto);
    }

    private Optional<SubscriptionDto> createAndSave(SubscriptionRequest request){
        Optional<Book> optionalBook = bookJpaRepository.findById(request.getBookId());
        Optional<User> optionalUser = userJpaRepository.findById(request.getUserId());
        if (optionalBook.isPresent() && optionalUser.isPresent()){
            Book book = optionalBook.get();
            User user = optionalUser.get();
            if (book.isAvailable()) {
                int amountTaken = takeBook(book, request.getCount());
                Subscription subscription = Subscription.builder()
                        .status(SubscriptionStatus.getSubscriptionStatus(request.getStatus()))
                        .created(LocalDateTime.now())
                        .deadline(LocalDateTime.now().plusDays(request.getDays()))
                        .returned(0)
                        .book(book)
                        .took(amountTaken)
                        .user(user)
                        .build();
                subscription = subscriptionJpaRepository.save(subscription);
                return Optional.of(subscription).map(s -> subscriptionMapper.toDto(s));
            }
        }
        return Optional.empty();
    }

    private int takeBook(Book book, int count) {
        int taken;
        if (book.getAvailableCount() >= count){
            book.setAvailableCount(book.getAvailableCount() - count);
            taken = count;
        } else {
            taken = book.getAvailableCount();
            book.setAvailableCount(0);
        }
        return taken;
    }

    @Override
    public Optional<SubscriptionDto> update(Long id, SubscriptionRequest request) {
        Optional<Subscription> optionalSubscription = subscriptionJpaRepository.findById(id);
        if (!optionalSubscription.isPresent()){
            return Optional.empty();
        }
        Subscription subscription = optionalSubscription.get();
        SubscriptionStatus gettingStatus = SubscriptionStatus.getSubscriptionStatus(request.getStatus());

        switch (gettingStatus) {
            case COMPLETED:
                prepareToCompletingSubscription(subscription);
                break;
            case READING:
                prepareToReadingSubscription(subscription);
                break;
            case READING_EXTENDED:
                prepareToExtendedSubscription(subscription);
                break;
        }
        return Optional.of(subscriptionMapper.toDto(subscriptionJpaRepository.save(subscription)));
    }

    private void prepareToExtendedSubscription(Subscription subscription) {

    }

    private void prepareToReadingSubscription(Subscription subscription) {

    }

    private void prepareToCompletingSubscription(Subscription subscription) {
    }

    @Override
    public Page<SubscriptionDto> findAll(Pageable pageable) {
        return null;
    }
}
