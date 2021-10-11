package by.it.academy.grodno.elibrary.api.dto.books;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
public class BookDetailsDto implements Serializable {

    private String title;
    private List<String> authors;
    private LocalDate datePublishing;
    private String pictureUrl;
}
