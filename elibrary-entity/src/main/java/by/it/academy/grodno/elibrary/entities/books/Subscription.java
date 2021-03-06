package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.entitymetadata.books.SubscriptionStatus;
import by.it.academy.grodno.elibrary.entitymetadata.converters.SubscriptionStatusConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = ("subscription"))
public class Subscription extends AEntity<Long> {

    @Column(name = "status_code")
    @Convert(converter = SubscriptionStatusConverter.class)
    private SubscriptionStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "took")
    private int took;

    @Column(name = "returned")
    private int returned;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(name = "subscription_created")
    private LocalDateTime created;

    @Column(name = "subscription_deadline")
    private LocalDateTime deadline;
}
