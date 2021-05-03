package by.it.academy.grodno.elibrary.web.responseentities;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class OpenLibraryBookResponse extends AEntityDto<Long> {

    private List<String> publishers;
    @JsonProperty("number_of_pages")
    private Integer printLength;
    @JsonProperty("isbn_10")
    private List<String> isbn10;
    private List<String> covers;
    private String key;
    @JsonProperty("authors")
    private List<Map<String, Object>> authorPath;
    @JsonProperty("languages")
    private List<Map<String, Object>> languagesPath;
    private String title;
    @JsonProperty("isbn_13")
    private List<String> isbn13;
    @JsonProperty("publish_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM d, yyyy", locale = "en")
    private LocalDate datePublishing;
    private List<Map<String, Object>> works;
    private Map<String, String> type;
    @JsonProperty("first_sentence")
    private Map<String, String> firstSentence;
    private Integer revision;
}
