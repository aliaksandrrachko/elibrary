package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class ReviewDto extends AEntityDto<Long> {

    @NotNull
    @Min(0)
    private Long userId;
    private String username;
    private String userEmail;
    private String userPictureUrl;

    @NotNull
    @Min(0)
    private Long bookId;
    private BookDetailsDto bookDetails;

    @NotBlank(message = "Review text can not be empty.")
    private String text;

    @Min(value = 0, message = "Min grade '0'")
    @Max(value = 5, message = "Max grade '5'")
    private byte grade;

    private LocalDateTime created;
    private LocalDateTime updated;
}
