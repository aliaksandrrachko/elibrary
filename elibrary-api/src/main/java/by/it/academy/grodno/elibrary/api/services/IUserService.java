package by.it.academy.grodno.elibrary.api.services;

import by.it.academy.grodno.elibrary.api.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService extends IAGenericCrudService<UserDto, Long> {

    Optional<UserDto> findUserByUserName(String username);
    Page<UserDto> findAll(Pageable pageable);
    Optional<UserDto> getUserBySocialId(Long socialId);
    Optional<UserDto> findUserByEmail(String email);
    Optional<UserDto> findById(String userId);
}
