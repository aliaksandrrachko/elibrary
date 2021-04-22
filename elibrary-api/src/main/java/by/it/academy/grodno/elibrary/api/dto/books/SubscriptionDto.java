package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SubscriptionDto extends AEntityDto<Long> {

    @NotBlank(message = "Field 'Status' can not be empty.")
    private String status;

    private Long userId;
    private String username;
    private String userEmail;

    @NotNull
    private Integer took;
    private int returned;

    private Long bookId;
    private BookDto bookDetails;

    private LocalDateTime created;
    private LocalDateTime deadline;
}
