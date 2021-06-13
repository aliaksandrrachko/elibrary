package by.it.academy.grodno.elibrary.rest.utils;

import static org.aspectj.util.LangUtil.isEmpty;

import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserJpaRepository userJpaRepository;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserJpaRepository userJpaRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String token = header.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        User user = userJpaRepository.findById(Long.valueOf(jwtTokenUtil.getUserId(token))).orElse(null);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                user == null ? null : String.valueOf(user.getId()),
                user == null ? null : user.getPassword(),
                user == null ? Collections.emptyList(): user.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
