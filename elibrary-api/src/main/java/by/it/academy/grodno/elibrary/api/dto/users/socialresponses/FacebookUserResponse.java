package by.it.academy.grodno.elibrary.api.dto.users.socialresponses;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacebookUserResponse extends AEntityDto<Long> {

    @JsonProperty("id")
    private Long socialId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("name")
    private String username;

    private String email;

    private Picture picture;
}
