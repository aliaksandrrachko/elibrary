package by.it.academy.grodno.elibrary.api.dto.books;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class OpenLibraryResponse extends AEntityDto<Long> {

    private String[] publishers;

    private Map<String, String> description;

    @JsonProperty("number_of_pages")
    private Integer printLength;

    @JsonProperty("isbn_10")
    private String[] isbn10;

    @JsonProperty("isbn_13")
    private String[] isbn13;

    private Map<String, String> authors;

    private String ocaid;

    private Map<String, String> languages;

    private String title;

    @JsonProperty("publish_date")
    private String datePublishing;
}
