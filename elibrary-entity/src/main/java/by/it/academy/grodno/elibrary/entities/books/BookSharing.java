package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import by.it.academy.grodno.elibrary.entities.converters.BookSharingStatusConverter;
import by.it.academy.grodno.elibrary.entities.users.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = ("sharing"))
@SecondaryTables(value = { @SecondaryTable(name = "sharing_has_book", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "book_id")})})
public class BookSharing extends AEntity<Integer> {

    @Column(name = "status_code")
    @Convert(converter = BookSharingStatusConverter.class)
    private BookSharingStatus status;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sharing_has_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "sharing_id"))
    private List<Book> books;

    @Column(name = "sharing_created")
    private LocalDateTime created;

    @Column(name = "sharing_deadline")
    private LocalDateTime deadline;
}
