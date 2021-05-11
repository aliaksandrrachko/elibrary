package by.it.academy.grodno.elibrary.rest;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "rest/users")
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@Valid @RequestBody UserDto dto) {
        return userService.create(dto).orElse(null);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUser(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        return userService.update(id, dto).orElse(null);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
