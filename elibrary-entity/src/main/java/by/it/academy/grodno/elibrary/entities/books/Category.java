package by.it.academy.grodno.elibrary.entities.books;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = {"categories", "parentCategory"})
@SuperBuilder
@Entity
@Table(name = "category")
// if you wand user entityGraph change fetch eager to lazy
@NamedEntityGraphs(value = {
        @NamedEntityGraph(name = "category-only-entity-graph"),
        @NamedEntityGraph(name = "category-categories-parentCategory-entity-graph",
            attributeNodes = {@NamedAttributeNode(value = "categories"),
            @NamedAttributeNode(value = "parentCategory")})
})
public class Category extends AEntity<Integer> {

    @Column(name = "category_name", length = 45)
    private String categoryName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Set<Category> categories;

    public Set<Category> getCategories(){
        if (categories == null) {
            categories = new HashSet<>();
        }
        return categories;
    }

    public String getPath(){
        StringBuilder sb = new StringBuilder();
        if (parentCategory != null){
            sb.append(parentCategory.getPath()).append('/');
            sb.append(parentCategory.getCategoryName());
        }
        return sb.toString();
    }
}
