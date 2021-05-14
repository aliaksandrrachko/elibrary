package by.it.academy.grodno.elibrary.service.services;

import by.it.academy.grodno.elibrary.api.dao.RoleJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.exceptions.PasswordMatchException;
import by.it.academy.grodno.elibrary.api.mappers.UserMapper;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.api.utils.DownloadFileType;
import by.it.academy.grodno.elibrary.api.utils.mail.IEmailSender;
import by.it.academy.grodno.elibrary.api.utils.mail.UserMailMessageType;
import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.Role;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.service.utils.FileUploader;
import by.it.academy.grodno.elibrary.service.utils.RandomPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements IUserService {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;
    private final RoleJpaRepository roleJpaRepository;
    private final IEmailSender emailSender;

    public UserService(PasswordEncoder bCryptPasswordEncoder, UserJpaRepository userJpaRepository, UserMapper userMapper,
                       RoleJpaRepository roleJpaRepository, IEmailSender emailSender) {
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
    public UserDto findUserByEmail(String email) {
        return userMapper.toDto(userJpaRepository.findByEmail(email).orElse(null));
    }

    @Override
    public UserDto findById(String userId) {
        Long userIdNumber = Long.parseLong(userId);
        return userMapper.toDto(userJpaRepository.findById(userIdNumber).orElse(null));
    }

    @Override
    public UserDto findUser(Principal principal) {
        UserDto userDto = null;
        if (principal != null && StringUtils.hasText(principal.getName())) {
            userDto = this.findById(principal.getName());
        }
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toDtos(userJpaRepository.findAll());
    }

    @Override
    public UserDto findById(Long id) {
        if (id != null) {
            return userMapper.toDto(userJpaRepository.findById(id).orElse(null));
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> optionalUser = userJpaRepository.findById(id);
        optionalUser.ifPresent(userJpaRepository::delete);
        optionalUser.ifPresent(user -> {
            String avatarUrl = user.getAvatarUrl();
            if (!avatarUrl.equals(DEFAULT_AVATAR_URL_FEMALE) &&
                    !avatarUrl.equals(DEFAULT_AVATAR_URL_MALE) &&
                    !avatarUrl.equals(DEFAULT_AVATAR_URL_UNKNOWN)) {
                FileUploader.deleteFile(avatarUrl);
            }
        });
    }

    @Override
    @Transactional
    public UserDto create(UserDto entityDto) {
        if (entityDto.getPassword().equals(entityDto.getPasswordConfirm())) {
            entityDto.setRoles(Collections.singleton("ROLE_USER"));
            entityDto.setUsername(entityDto.getFirstName() + " " + entityDto.getLastName());
            entityDto.setCreated(LocalDateTime.now().withNano(0));
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            entityDto.getAddressDto().setUpdated(LocalDateTime.now().withNano(0));
            User user = userMapper.toEntity(entityDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            setDefaultAvatarImageIfNot(user);
            user = userJpaRepository.save(user);
            emailSender.sendEmailFromAdmin(user, UserMailMessageType.REGISTERED, null);
            return userMapper.toDto(user);
        } else {
            throw new PasswordMatchException(entityDto);
        }
    }

    @Override
    @Transactional
    public Optional<User> createUserFromSocialNetwork(User user) {
        user.setCreated(LocalDateTime.now().withNano(0));
        user.setUpdated(LocalDateTime.now().withNano(0));
        String randomPassword = RandomPasswordGenerator.generateRandomPassword();
        user.setPassword(bCryptPasswordEncoder.encode(randomPassword));
        Set<Role> roleSet = new HashSet<>();
        user.getRoles().forEach(r -> roleJpaRepository.findByRoleName(r.getRoleName()).ifPresent(roleSet::add));
        user.setRoles(roleSet);
        setDefaultAvatarImageIfNot(user);
        user = userJpaRepository.save(user);
        emailSender.sendEmailFromAdmin(user, UserMailMessageType.REGISTERED_WITH_PASSWORD, Collections.singletonMap("password", randomPassword));
        return Optional.of(user);
    }

    private static final String DEFAULT_AVATAR_URL_MALE = "/img/users/avatars/default_male_avatar.png";
    private static final String DEFAULT_AVATAR_URL_FEMALE = "/img/users/avatars/default_female_avatar.png";
    private static final String DEFAULT_AVATAR_URL_UNKNOWN = "/img/users/avatars/default_unknown_avatar.png";

    private void setDefaultAvatarImageIfNot(User user){
        if (!StringUtils.hasText(user.getAvatarUrl())){
            switch (user.getGender()){
                case MALE: user.setAvatarUrl(DEFAULT_AVATAR_URL_MALE);
                return;
                case FEMALE: user.setAvatarUrl(DEFAULT_AVATAR_URL_FEMALE);
                return;
                default: user.setAvatarUrl(DEFAULT_AVATAR_URL_UNKNOWN);
            }
        }
    }

    @Override
    public Optional<User> findByEmailOrSocialId(String email, Long socialId) {
        return userJpaRepository.findByEmailOrSocialId(email, socialId);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto entityDto) {
        User user = prepareUserToUpdating(id, entityDto);
        if (user != null){
            user = userJpaRepository.save(user);
        }
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public Optional<UserDto> update(Long id, UserDto userDto, MultipartFile file) {
        User user = prepareUserToUpdating(id, userDto);
        if (user == null){
            return Optional.empty();
        }
        if (file != null && !file.isEmpty()){
            try {
                URL avatarUrl = FileUploader.uploadFile(file, DownloadFileType.USER_AVATAR, String.valueOf(user.getId()));
                user.setAvatarUrl(avatarUrl.toString());
            } catch (IOException e) {
                log.error("Error uploading users avatar image : '{}'.", file.getName());
            }
        }
        user = userJpaRepository.save(user);
        return Optional.of(userMapper.toDto(user));
    }

    private User prepareUserToUpdating(Long id, UserDto entityDto){
        Optional<User> optionalUser = userJpaRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return null;
        }
        if (entityDto.getPassword().equals(entityDto.getPasswordConfirm())) {
            User userFromDb = optionalUser.get();
            entityDto.setId(id);
            if (!StringUtils.hasText(entityDto.getUsername())) {
                entityDto.setUsername(entityDto.getFirstName() + " " + entityDto.getLastName());
            }
            entityDto.setCreated(userFromDb.getCreated());
            entityDto.setUpdated(LocalDateTime.now().withNano(0));
            entityDto.setSocialId(userFromDb.getSocialId());
            entityDto.getAddressDto().setUpdated(LocalDateTime.now().withNano(0));
            if (entityDto.getAddressDto().isFullAddress()) {
                entityDto.getRoles().add("ROLE_USER");
            }
            entityDto.getRoles().addAll(userFromDb.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));
            setAddressIdIfExists(userFromDb, entityDto);
            User user = userMapper.toEntity(entityDto);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            setDefaultAvatarImageIfNot(user);
            return user;
        } else {
            throw new PasswordMatchException(entityDto);
        }
    }

    private void setAddressIdIfExists(User userFromDb, UserDto entityDto) {
        Address address = userFromDb.getAddress();
        if (address != null) {
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
