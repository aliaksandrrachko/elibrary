package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Subscription;
import by.it.academy.grodno.elibrary.entities.books.SubscriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SubscriptionJpaRepository extends JpaRepository<Subscription, Long> {

    Page<Subscription> findAllByStatus(SubscriptionStatus status, Pageable pageable);

    Page<Subscription> findByUserId(Long userId, Pageable pageable);

    Page<Subscription> findByStatusAndUserId(SubscriptionStatus status, Long userId, Pageable pageable);

    Page<Subscription> findByUserIdAndDeadlineAfterAndStatusIsNot(Long userId,
                                                                  LocalDateTime deadline,
                                                                  SubscriptionStatus status,
                                                                  Pageable pageable);
}
