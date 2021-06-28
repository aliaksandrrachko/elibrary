package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
public class UserRestController {

    private final IUserService userService;

    public UserRestController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = Routes.User.USER_PROFILE)
    public UserDto findCurrentUser(Principal principal){
        return userService.findById(principal.getName());
    }

    @PutMapping(value = Routes.User.USER_PROFILE)
    public UserDto updateCurrentUser(@Valid @ModelAttribute(value = "userDto") UserDto userDto,
                                     @RequestParam(value = "fileAvatar", required = false) MultipartFile file,
                                     Principal principal, HttpServletResponse response) {
        UserDto updatedUser = null;
        Optional<UserDto> optionalUpdatedUser = this.userService.update(Long.valueOf(principal.getName()), userDto, file);
        optionalUpdatedUser.ifPresent(this::changeUserInSecurityContext);
        if (optionalUpdatedUser.isPresent()) {
            updatedUser = optionalUpdatedUser.get();
            this.changeUserInSecurityContext(updatedUser);
        }
        return updatedUser;
    }

    @GetMapping(value = Routes.User.ADMIN_USERS)
    public Page<UserDto> findAllUser(@PageableDefault(sort = {"username"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping(value = Routes.User.ADMIN_USERS_ID)
    public UserDto findUserById(@PathVariable Long id){
       return userService.findById(id);
    }

    @GetMapping(value = Routes.User.ADMIN_USERS_EMAIL)
    public UserDto findUserById(@PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @PostMapping(value = Routes.User.ADMIN_USERS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PutMapping(value = Routes.User.ADMIN_USERS_ID, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping(value = Routes.User.ADMIN_USERS_ID)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    private void changeUserInSecurityContext(UserDto updatedUser) {
        Collection<? extends GrantedAuthority> nowAuthorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        Set<SimpleGrantedAuthority> newAuthorities = new HashSet<>();
        nowAuthorities.forEach(grantedAuthority -> newAuthorities.add(new SimpleGrantedAuthority(grantedAuthority.getAuthority())));
        updatedUser.getRoles().forEach(role -> newAuthorities.add(new SimpleGrantedAuthority(role)));

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        String.valueOf(updatedUser.getId()),
                        updatedUser.getPassword(),
                        newAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
