package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "publisher")
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "publisher-only-entity-graph"),
        @NamedEntityGraph(name = "publisher-books-entity-graph", attributeNodes = {@NamedAttributeNode("books")})
})
public class Publisher extends AEntity<Integer> {

    @Column(name = "publisher_name")
    private String publisherName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Set<Book> books;
}
