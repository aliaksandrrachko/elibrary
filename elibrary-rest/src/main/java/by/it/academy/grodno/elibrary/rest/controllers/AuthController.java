package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.api.dto.auth.AuthRequest;
import by.it.academy.grodno.elibrary.api.dto.auth.AuthResponse;
import by.it.academy.grodno.elibrary.api.dto.users.UserDto;
import by.it.academy.grodno.elibrary.api.services.IUserService;
import by.it.academy.grodno.elibrary.rest.utils.JwtTokenUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService userService;


    public AuthController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, IUserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping(Routes.Auth.LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            String token = jwtTokenUtil.generateAccessToken(authenticate);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
                    .body(new AuthResponse(authenticate.getName(), token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(Routes.Auth.SIGN_UP)
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid UserDto userDto){
        UserDto createdUser = userService.create(userDto);
        return ResponseEntity.ok().body(createdUser);
    }
}
