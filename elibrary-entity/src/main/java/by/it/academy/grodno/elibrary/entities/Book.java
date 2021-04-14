package by.it.academy.grodno.elibrary.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = ("book"))
@SecondaryTables(value = { @SecondaryTable(name = "book_has_author", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "book_id")})})
public class Book extends AEntity<Long>{

    @Column(name = "isbn_10", length = 10)
    private String isbn10;

    @Column(name = "isbn_13", length = 13)
    private String isbn13;

    @OneToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private BookCategorySection section;
}
