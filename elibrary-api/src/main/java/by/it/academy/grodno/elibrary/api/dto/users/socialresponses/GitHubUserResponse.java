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
public class GitHubUserResponse extends AEntityDto<Long> {

    @JsonProperty("id")
    private Long socialId;

    @JsonProperty("login")
    private String username;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    private String email;
}
