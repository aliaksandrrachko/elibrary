package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/users")
public class UserRestController {

    private final IUserService userService;

    public UserRestController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDto> findAllUser() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public UserDto findUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public Page<UserDto> findAllUser(@RequestParam(value = "userId", required = false) Long userId,
                                     @RequestParam(value = "email", required = false) String email,
                                     @PageableDefault(sort = {"username"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserDto> pageUsersDto;
        UserDto foundUser;
        if (userId != null) {
            foundUser = userService.findById(userId);
        } else if (email != null) {
            foundUser = userService.findUserByEmail(email);
        } else {
            foundUser = null;
        }

        if (foundUser != null) {
            pageUsersDto = new PageImpl<>(Collections.singletonList(foundUser), pageable, 1L);
        } else {
            pageUsersDto = userService.findAll(pageable);
        }
        return pageUsersDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        return userService.create(dto);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
