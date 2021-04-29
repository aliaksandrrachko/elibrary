package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "book")
@SecondaryTables(value = {
        @SecondaryTable(name = "category", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")}),
        @SecondaryTable(name = "publisher", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})})
public class Book extends AEntity<Long> {
    @Column(name = "isbn_10", length = 10)
    private String isbn10;

    @Column(name = "isbn_13", length = 13)
    private String isbn13;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "book_has_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

//    @Type(type = "json", typeClass = JsonStringType.class)
//    @JoinTable(name = "attribute",
//            joinColumns = {@JoinColumn(name = "attribute", columnDefinition = "json")})
//    private Map<String, Object> attributes;

    /**
     * Code by alpha-3/ISO 639-2.
     */
    @Column(name = "language", length = 3)
    private String language;

    @Column(name = "publishing_date")
    private LocalDate datePublishing;

    @Column(name = "print_length")
    private int printLength;

    @Column(name = "picture_url")
    private String pictureUrl;

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "available_count")
    private int availableCount;

    @Column(name = "available")
    private boolean available;

    @Column(name = "book_rating", updatable = false)
    private int rating;

    @Column(name = "book_created", insertable = false, updatable = false)
    private LocalDateTime created;

    @Column(name = "book_updated", insertable = false, updatable = false)
    private LocalDateTime updated;

    public boolean isAvailable(){
        return available && availableCount > 0;
    }

    public Set<Author> getAuthors() {
        if (authors == null){
            return authors = new HashSet<>();
        } else {
            return authors;
        }
    }
}
