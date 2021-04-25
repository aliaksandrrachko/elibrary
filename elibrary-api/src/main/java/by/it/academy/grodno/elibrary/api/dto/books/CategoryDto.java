package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CategoryDto extends AEntityDto<Integer> {

    private Integer parentId;
    private String parentCategory;

    private String categoryPath;

    @NotBlank(message = "Field 'Category' can not be empty.")
    private String categoryName;

    private Set<CategoryDto> categories;
}
