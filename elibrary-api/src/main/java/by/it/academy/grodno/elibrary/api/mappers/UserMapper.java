package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.UserDto;
import by.it.academy.grodno.elibrary.entities.users.Gender;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserMapper extends AGenericMapper<User, UserDto, Long>{

    public UserMapper(ModelMapper modelMapper) {
        super(modelMapper, User.class, UserDto.class);;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(u -> u.skip(UserDto::setGender)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> m.skip(User::setGender)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(User source, UserDto destination) {
        destination.setGender(source.getGender().getAbbreviation());
    }

    @Override
    public void mapSpecificFields(UserDto source, User destination) {
        destination.setGender(Gender.getGender(source.getGender()));
    }
}
