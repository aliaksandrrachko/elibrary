package by.it.academy.grodno.elibrary.api.services;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

public interface IUserService extends IAGenericCrudService<UserDto, Long> {

    Page<UserDto> findAll(Pageable pageable);
    UserDto findUserByEmail(String email);
    UserDto findById(String userId);
    UserDto findUser(Principal principal);
    Optional<User> createUserFromSocialNetwork(User user);
    Optional<User> findByEmailOrSocialId(String email, Long socialId);
    void deleteRole(long userId, String roleName);
    void addRole(long userId, String roleName);
    void setAvailability(long userId);
    Optional<UserDto> update(Long valueOf, UserDto userDto, MultipartFile file);
}
