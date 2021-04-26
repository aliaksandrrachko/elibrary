package by.it.academy.grodno.elibrary.service.services;

import by.it.academy.grodno.elibrary.api.dao.RoleJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.mappers.UserMapper;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.Role;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.service.exceptions.PasswordMatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleJpaRepository roleJpaRepository;

    @Override
    public Optional<UserDto> findUserByUserName(String username) {
        Optional<User> optionalUser = userJpaRepository.findByUsername(username);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> user = userJpaRepository.findAll(pageable);
        return userMapper.toPageDto(user);
    }

    @Override
    public Optional<UserDto> getUserBySocialId(Long socialId) {
        Optional<User> optionalUser = userJpaRepository.findBySocialId(socialId);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        Optional<User> optionalUser = userJpaRepository.findByEmail(email);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    @Override
    public Optional<UserDto> findById(String userId) {
        Long userIdNumber = Long.parseLong(userId);
        Optional<User> optionalUser = userJpaRepository.findById(userIdNumber);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    @Override
    public Optional<UserDto> findUser(Principal principal) {
        Optional<UserDto> optionalUserDto = Optional.empty();
        if (principal != null && StringUtils.hasText(principal.getName())) {
            optionalUserDto = this.findById(principal.getName());
        }
        return optionalUserDto;
    }

    @Override
    public Class<UserDto> getGenericClass() {
        return UserDto.class;
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toDtos(userJpaRepository.findAll());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<User> optionalUser = userJpaRepository.findById(id);
        return optionalUser.map(user -> userMapper.toDto(user));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> optionalUser = userJpaRepository.findById(id);
        optionalUser.ifPresent(user -> userJpaRepository.delete(user));
    }

    @Override
    @Transactional
    public Optional<UserDto> create(UserDto entityDto) {
  /*      if (entityDto.getPassword() == null && entityDto.getPasswordConfirm() == null) {

        } */
        if (entityDto.getPassword().equals(entityDto.getPasswordConfirm())){
            entityDto.setRoles(Collections.singleton("ROLE_USER"));
            entityDto.setUsername(entityDto.getFirstName() + " " + entityDto.getLastName());
            entityDto.setCreated(LocalDateTime.now().withNano(0));
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            entityDto.getAddressDto().setUpdated(LocalDateTime.now().withNano(0));
            User user = userMapper.toEntity(entityDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userJpaRepository.save(user);
            return Optional.of(userMapper.toDto(user));
        } else  {
            throw new PasswordMatchException(entityDto);
        }
    }

    @Override
    @Transactional
    public Optional<UserDto> update(Long id, UserDto entityDto) {
        Optional<User> optionalUser = userJpaRepository.findById(id);
        if (!optionalUser.isPresent()){
            return Optional.empty();
        }
        if (entityDto.getPassword().equals(entityDto.getPasswordConfirm())){
            User userFromDb = optionalUser.get();
            entityDto.setId(id);
            if (!StringUtils.hasText(entityDto.getUsername())) {
                entityDto.setUsername(entityDto.getFirstName() + " " + entityDto.getLastName());
            }
            entityDto.setCreated(userFromDb.getCreated());
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            entityDto.setSocialId(userFromDb.getSocialId());
            entityDto.getAddressDto().setUpdated(LocalDateTime.now().withNano(0));
            if (entityDto.getAddressDto().isFullAddress()){
                entityDto.getRoles().add("ROLE_USER");
            }
            entityDto.getRoles().addAll(userFromDb.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));
            setAddressIdIfExists(userFromDb, entityDto);
            User user = userMapper.toEntity(entityDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(userFromDb.getRoles());
            user = userJpaRepository.save(user);
            return Optional.of(userMapper.toDto(user));
        } else {
            throw new PasswordMatchException(entityDto);
        }
    }

    private void setAddressIdIfExists(User userFromDb, UserDto entityDto){
        Address address = userFromDb.getAddress();
        if (address != null){
            entityDto.getAddressDto().setId(userFromDb.getAddress().getId());
        }
    }

    @Override
    @Transactional
    public void deleteRole(long userId, String roleName) {
        Optional<User> userOptionalFromDB = userJpaRepository.findById(userId);
        userOptionalFromDB.ifPresent(user -> {
            user.setRoles(user.getRoles().stream().filter(r -> !r.getRoleName().equals(roleName)).collect(Collectors.toSet()));
            userJpaRepository.save(user);
        });
    }


    @Override
    @Transactional
    public void addRole(long userId, String roleName) {
        Optional<User> userOptionalFromDB = userJpaRepository.findById(userId);
        userOptionalFromDB.ifPresent(user -> {
            roleJpaRepository.findByRoleName(roleName).ifPresent(r -> user.getRoles().add(r));
            userJpaRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void setAvailability(long userId) {
        Optional<User> userOptionalFromDB = userJpaRepository.findById(userId);
        userOptionalFromDB.ifPresent(user -> {
            user.setEnabled(!user.isEnabled());
            userJpaRepository.save(user);
        });
    }
}
