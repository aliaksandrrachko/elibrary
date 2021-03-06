package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.RoleJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.users.AddressDto;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.entities.users.*;
import by.it.academy.grodno.elibrary.entitymetadata.users.Gender;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper extends AGenericMapper<User, UserDto, Long>{

    private final RoleJpaRepository roleJpaRepository;

    private final AddressMapper addressMapper;
    private final PhoneNumberMapper phoneNumberMapper;

    public UserMapper(ModelMapper modelMapper, RoleJpaRepository roleJpaRepository, AddressMapper addressMapper,
                      PhoneNumberMapper phoneNumberMapper) {
        super(modelMapper, User.class, UserDto.class);
        this.roleJpaRepository = roleJpaRepository;
        this.addressMapper = addressMapper;
        this.phoneNumberMapper = phoneNumberMapper;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(User.class, UserDto.class)
                .addMappings(u -> {
                    u.skip(UserDto::setGender);
                    u.skip(UserDto::setAddressDto);
                    u.skip(UserDto::setRoles);
                    u.skip(UserDto::setPhoneNumber);
                    u.skip(UserDto::setPassword);
                    u.skip(UserDto::setPasswordConfirm);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> {
                    m.skip(User::setGender);
                    m.skip(User::setAddress);
                    m.skip(User::setPhoneNumber);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(User source, UserDto destination) {
        destination.setGender(source.getGender().name().toLowerCase());
        setAddressToUserDto(source, destination);
        destination.setRoles(source.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        destination.setPhoneNumber(phoneNumberMapper.toDto(source.getPhoneNumber()));
    }

    @Override
    public void mapSpecificFields(UserDto source, User destination) {
        destination.setGender(Gender.getGender(source.getGender().charAt(0)));
        destination.setAddress(addressMapper.toEntity(source.getAddressDto()));

        Set<Role> roles = new HashSet<>();
        source.getRoles().forEach(role -> {
            Optional<Role> roleOptional = roleJpaRepository.findByRoleName(role);
            roleOptional.ifPresent(roles::add);
        });

        destination.setRoles(roles);
        destination.setPhoneNumber(phoneNumberMapper.toEntity(source.getPhoneNumber()));
    }

    private void setAddressToUserDto(User source, UserDto destination){
        Address address = source.getAddress();
        if (address == null){
            destination.setAddressDto(new AddressDto());
        } else {
            destination.setAddressDto(addressMapper.toDto(source.getAddress()));
        }
    }
}
