package by.it.academy.grodno.elibrary.service.services;

import by.it.academy.grodno.elibrary.api.dto.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto findUserByUserName(String username) {
        return null;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<UserDto> getUserBySocialId(Long socialId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Class<UserDto> getGenericClass() {
        return null;
    }

    @Override
    public List<UserDto> findAll() {
        return null;
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<UserDto> create(UserDto entityDto) {
        return null;
    }

    @Override
    public Optional<UserDto> update(Long id, UserDto entityDto) {
        return null;
    }
}
