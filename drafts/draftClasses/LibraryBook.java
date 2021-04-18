package by.it.academy.grodno.elibrary.entities.books;

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
public class LibraryBook extends BookDetails{

    @Column(name = "total_count")
    private int totalCount;

    @Column(name = "available_count")
    private int availableCount;

    @Column(name = "available")
    private boolean available;

    @Column(name = "book_created")
    private LocalDateTime created;

    @Column(name = "book_updated")
    private LocalDateTime updated;
}
