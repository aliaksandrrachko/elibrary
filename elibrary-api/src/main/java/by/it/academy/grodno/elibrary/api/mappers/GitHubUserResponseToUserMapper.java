package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.users.socialresponses.GitHubUserResponse;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GitHubUserResponseToUserMapper extends AGenericMapper<User, GitHubUserResponse, Long> {

    protected GitHubUserResponseToUserMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, GitHubUserResponse.class);
    }
}
