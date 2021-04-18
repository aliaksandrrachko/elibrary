package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AuthorDto extends AEntityDto<Integer> {

    @NotBlank(message = "Field 'Author' can not be empty.")
    private String authorName;
}
