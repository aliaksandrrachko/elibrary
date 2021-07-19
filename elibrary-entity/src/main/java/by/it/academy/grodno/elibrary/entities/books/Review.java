package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import by.it.academy.grodno.elibrary.entities.users.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "review")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "review-user-book-entity-graph",
                attributeNodes = {@NamedAttributeNode(value = "user", subgraph = "user-only-entity-graph"),
                    @NamedAttributeNode(value = "book", subgraph = "book-authors-entity-graph")})
})
public class Review extends AEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Column(name = "review_text", columnDefinition = "text")
    private String text;

    @Column(name = "review_grade")
    private byte grade;

    @Column(name = "review_created", insertable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "review_updated", insertable = false, updatable = false)
    private LocalDateTime updated;
}