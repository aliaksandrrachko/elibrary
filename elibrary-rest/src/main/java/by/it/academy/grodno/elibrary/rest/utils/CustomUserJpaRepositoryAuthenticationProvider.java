package by.it.academy.grodno.elibrary.rest.utils;

import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserJpaRepositoryAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserJpaRepository userJpaRepository;

    public CustomUserJpaRepositoryAuthenticationProvider(PasswordEncoder bCryptPasswordEncoder, UserJpaRepository userJpaRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("No credentials found in context");
        }

        Optional<User> userOptionalFromDb = userJpaRepository.findByEmail(username);

        if (!userOptionalFromDb.isPresent()) {
            throw new BadCredentialsException("User does not exist");
        }

        User user = userOptionalFromDb.get();
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            user.getAuthorities();
        } else {
            throw new BadCredentialsException("Password incorrect");
        }

        check(user);

        return new UsernamePasswordAuthenticationToken(String.valueOf(user.getId()), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
    }
}