package by.it.academy.grodno.elibrary.api.services.books;

import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionRequest;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISubscriptionService {

    List<SubscriptionDto> findAll();
    Optional<SubscriptionDto> findById(Long id);
    void delete(Long id);

    Optional<SubscriptionDto> booking(SubscriptionRequest entityDto);

    Optional<SubscriptionDto> create(SubscriptionRequest entityDto);
    Optional<SubscriptionDto> update(Long id, SubscriptionRequest entityDto);
    Page<SubscriptionDto> findAll(Pageable pageable);
}
