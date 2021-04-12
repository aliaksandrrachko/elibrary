package by.it.academy.grodno.elibrary.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = ("category"))
public class BookCategory extends AEntity<Integer> implements Serializable {

    @Column(name = "category_name", length = 45)
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "section",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<BookCategorySection> sections;
}
