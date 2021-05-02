package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.users.socialresponses.FacebookUserResponse;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FacebookUserResponseToUserMapper extends AGenericMapper<User, FacebookUserResponse, Long>{

    protected FacebookUserResponseToUserMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, FacebookUserResponse.class);
    }
}
