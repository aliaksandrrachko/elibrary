package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = "author")
public class Author extends AEntity<Integer> {

    @Column(name = "author_name")
    private String authorName;
}
