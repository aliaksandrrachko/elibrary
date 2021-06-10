package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/signup")
public class SignUpController {

    private final IUserService userService;

    public SignUpController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<UserDto> signUp(@Valid @ModelAttribute(value = "userDto") UserDto userDto, BindingResult result) {
        ResponseEntity<UserDto> responseEntity;
        try {
            if (result.hasErrors()) {
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                UserDto createdUser = userService.create(userDto);
                responseEntity = new ResponseEntity<>(createdUser, HttpStatus.OK);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}