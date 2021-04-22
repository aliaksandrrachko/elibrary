package by.it.academy.grodno.elibrary.api.dto.books;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {

    @Min(0L)
    private long userId;

    @Builder.Default
    private int status = 1;

    @Builder.Default
    private int days = 1;

    @Min(0L)
    private long bookId;

    @Builder.Default
    @Min(1)
    private int count = 1;
}
