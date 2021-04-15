package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = ("book"))
@SecondaryTables(value = { @SecondaryTable(name = "book_has_author", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "book_id")})})
public class Book extends AEntity<Long> {

    @Column(name = "isbn_10", length = 10)
    private String isbn10;

    @Column(name = "isbn_13", length = 13)
    private String isbn13;

    @OneToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private BookCategorySection section;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_has_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @OneToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @Column(name = "language", length = 3)
    private String language;

    @Column(name = "date_publishing")
    private LocalDate datePublishing;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "available_count")
    private int availableCount;

    @Column(name = "available")
    private boolean available;

    @Column(name = "book_rating")
    private int bookRating;

    @Column(name = "book_created")
    private LocalDate created;

    @Column(name = "book_updated")
    private LocalDate updated;
}
