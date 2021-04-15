package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
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
@Table(name = ("section"))
public class BookCategorySection extends AEntity<Integer> {

    @Column(name = "section_name", length = 45)
    private String sectionName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "category_id", nullable = false)
    private BookCategory category;
}
