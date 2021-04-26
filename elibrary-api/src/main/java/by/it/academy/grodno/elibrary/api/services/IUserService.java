package by.it.academy.grodno.elibrary.api.services;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

public interface IUserService extends IAGenericCrudService<UserDto, Long> {

    Optional<UserDto> findUserByUserName(String username);
    Page<UserDto> findAll(Pageable pageable);
    Optional<UserDto> getUserBySocialId(Long socialId);
    Optional<UserDto> findUserByEmail(String email);
    Optional<UserDto> findById(String userId);
    Optional<UserDto> findUser(Principal principal);

    void deleteRole(long userId, String roleName);

    void addRole(long userId, String roleName);

    @Transactional
    void setAvailability(long userId);
}
