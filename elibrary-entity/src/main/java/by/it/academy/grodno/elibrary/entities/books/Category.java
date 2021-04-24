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
@EqualsAndHashCode(callSuper = true, exclude = {"categories", "parentCategory"})
@SuperBuilder
@Entity
@Table(name = ("category"))
public class Category extends AEntity<Integer> implements Serializable {

    @Column(name = "category_name", length = 45)
    private String categoryName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Set<Category> categories;

    public Set<Category> getCategories(){
        if (categories != null){
            return categories;
        } else {
            return new HashSet<>();
        }
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
