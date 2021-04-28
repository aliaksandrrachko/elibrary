package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISubscriptionService {

    List<SubscriptionDto> findAll();
    Optional<SubscriptionDto> findById(Long id);
    void delete(Long id);
    Optional<SubscriptionDto> booking(SubscriptionRequest request);
    Optional<SubscriptionDto> create(SubscriptionRequest request);
    Optional<SubscriptionDto> update(Long id, SubscriptionRequest request);
    Page<SubscriptionDto> findAll(Pageable pageable);
    Page<SubscriptionDto> findAllByUserId(Long userId, Pageable pageable);
    Page<SubscriptionDto> findAllByUserIdAndStatus(Long userId, Integer statusCode, Pageable pageable);
    Page<SubscriptionDto> findAllByStatus(Integer statusCode, Pageable pageable);
    Optional<SubscriptionDto> undoBooking(SubscriptionRequest request);
}