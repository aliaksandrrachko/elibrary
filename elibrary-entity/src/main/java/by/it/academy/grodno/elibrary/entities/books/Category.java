package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = "sections")
@SuperBuilder
@Entity
@Table(name = ("category"))
public class Category extends AEntity<Integer> implements Serializable {

    @Column(name = "category_name", length = 45)
    private String categoryName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Set<Section> sections;

    public Set<Section> getSections(){
        if (sections != null){
            return sections;
        } else {
            return new HashSet<>();
        }
    }
}
