package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.users.socialresponses.FacebookUserResponse;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FacebookUserResponseToUserMapper extends AGenericMapper<User, FacebookUserResponse, Long>{

    protected FacebookUserResponseToUserMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, FacebookUserResponse.class);
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, FacebookUserResponse.class)
                .addMappings(u -> {}).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(FacebookUserResponse.class, User.class)
                .addMappings(m -> m.skip(User::setAvatarUrl))
                .setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FacebookUserResponse source, User destination) {
        destination.setAvatarUrl(source.getPicture().getData().getUrl());
    }
}
