package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class BookDto extends AEntityDto<Long> {

    @ISBN(message = "Wrong ISBN-10", type = ISBN.Type.ISBN_10)
    private String isbn10;

    @ISBN(message = "Wrong ISBN-13", type = ISBN.Type.ISBN_13)
    private String isbn13;

    @NotBlank(message = "Field 'Title' can not be empty.")
    private String title;

    private String description;

    private CategoryDto category;

    private String publisher;

    private Set<String> authors;

    @Pattern(regexp = "^\\w{3}$", message = "Code by alpha-3/ISO 639-2.")
    private String language;

    @NotNull
    @NotNull(message = "Field 'Publishing date' can not be empty.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate datePublishing;

    private int printLength;

    private String pictureUrl;

    private int totalCount;

    private int availableCount;

    private boolean available;

    private int rating;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime created;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updated;
}
