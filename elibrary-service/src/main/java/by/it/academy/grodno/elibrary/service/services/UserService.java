package by.it.academy.grodno.elibrary.service.services;

import by.it.academy.grodno.elibrary.api.dao.RoleJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.mappers.UserMapper;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.utils.mail.IEmailSender;
import by.it.academy.grodno.elibrary.api.utils.mail.UserMailMessageType;
import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.Role;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.service.exceptions.PasswordMatchException;
import by.it.academy.grodno.elibrary.service.utils.RandomPasswordGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final RoleJpaRepository roleJpaRepository;
    private final IEmailSender emailSender;

    public UserService(PasswordEncoder bCryptPasswordEncoder, UserJpaRepository userJpaRepository, UserMapper userMapper, RoleJpaRepository roleJpaRepository, IEmailSender emailSender) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
        this.roleJpaRepository = roleJpaRepository;
        this.emailSender = emailSender;
    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> user = userJpaRepository.findAll(pageable);
        return userMapper.toPageDto(user);
    }

    @Override
    public Optional<UserDto> findUserByEmail(String email) {
        Optional<User> optionalUser = userJpaRepository.findByEmail(email);
        return optionalUser.map(userMapper::toDto);
    }

    @Override
    public Optional<UserDto> findById(String userId) {
        Long userIdNumber = Long.parseLong(userId);
        Optional<User> optionalUser = userJpaRepository.findById(userIdNumber);
        return optionalUser.map(userMapper::toDto);
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
        if (id != null) {
            return userJpaRepository.findById(id).map(userMapper::toDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> optionalUser = userJpaRepository.findById(id);
        optionalUser.ifPresent(userJpaRepository::delete);
    }

    @Override
    @Transactional
    public Optional<UserDto> create(UserDto entityDto) {
        if (entityDto.getPassword().equals(entityDto.getPasswordConfirm())){
            entityDto.setRoles(Collections.singleton("ROLE_USER"));
            entityDto.setUsername(entityDto.getFirstName() + " " + entityDto.getLastName());
            entityDto.setCreated(LocalDateTime.now().withNano(0));
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            entityDto.getAddressDto().setUpdated(LocalDateTime.now().withNano(0));
            User user = userMapper.toEntity(entityDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user = userJpaRepository.save(user);
            emailSender.sendEmailFromAdmin(user, UserMailMessageType.REGISTERED, null);
            return Optional.of(userMapper.toDto(user));
        } else  {
            throw new PasswordMatchException(entityDto);
        }
    }

    @Override
    @Transactional
    public Optional<User> createUserFromSocialNetwork(User user){
        user.setCreated(LocalDateTime.now().withNano(0));
        user.setUpdated(LocalDateTime.now().withNano(0));
        String randomPassword = RandomPasswordGenerator.generateRandomPassword();
        user.setPassword(bCryptPasswordEncoder.encode(randomPassword));
        Set<Role> roleSet = new HashSet<>();
        user.getRoles().forEach(r -> roleJpaRepository.findByRoleName(r.getRoleName()).ifPresent(roleSet::add));
        user.setRoles(roleSet);
        user = userJpaRepository.save(user);
        emailSender.sendEmailFromAdmin(user, UserMailMessageType.REGISTERED_WITH_PASSWORD, Collections.singletonMap("password", randomPassword));
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmailOrSocialId(String email, Long socialId){
        return userJpaRepository.findByEmailOrSocialId(email, socialId);
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
