package by.it.academy.grodno.elibrary.web.responseentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OpenLibraryAuthorResponse {

    private Map<String, String> bio;

    @JsonProperty("name")
    private String authorName;

    @JsonProperty("personal_name")
    private String personalName;
}
