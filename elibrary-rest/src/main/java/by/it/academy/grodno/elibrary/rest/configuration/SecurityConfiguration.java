package by.it.academy.grodno.elibrary.rest.configuration;

import static by.it.academy.grodno.elibrary.api.Role.ROLE_ADMIN;
import static by.it.academy.grodno.elibrary.api.Role.ROLE_USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.rest.utils.CustomUserJpaRepositoryAuthenticationProvider;
import by.it.academy.grodno.elibrary.rest.utils.JwtAuthenticationFilter;
import by.it.academy.grodno.elibrary.rest.utils.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("by.it.academy.grodno.elibrary.rest.utils")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserJpaRepositoryAuthenticationProvider customUserJpaRepositoryAuthenticationProvider;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Public endpoints
                .antMatchers("/", "/v2/api-docs", Routes.Auth.LOGIN, Routes.Auth.SIGN_UP, "/auth").permitAll()
                // Authentication endpoints
                .antMatchers(
                        Routes.Author.AUTHORS_ANY,
                        Routes.Book.BOOKS_ANY,
                        Routes.Category.CATEGORIES_ANY,
                        Routes.Publisher.PUBLISHERS_ANY,
                        Routes.User.USER_PROFILE).authenticated()
                .antMatchers(
                        GET,
                        Routes.Review.REVIEWS_ANY,
                        Routes.Subscription.SUBSCRIPTIONS_ANY).authenticated()
                // Full user endpoints
                .antMatchers(
                        POST,
                        Routes.Review.REVIEWS_ANY,
                        Routes.Subscription.SUBSCRIPTIONS_ANY).hasAnyAuthority(ROLE_USER.name(), ROLE_ADMIN.name())
                .antMatchers(
                        PUT,
                        Routes.Review.REVIEWS_ANY).hasAnyAuthority(ROLE_USER.name(), ROLE_ADMIN.name())
                .antMatchers(
                        DELETE,
                        Routes.Review.REVIEWS_ANY,
                        Routes.Subscription.SUBSCRIPTIONS_ANY).hasAnyAuthority(ROLE_USER.name(), ROLE_ADMIN.name())
                // Admin endpoints
                .antMatchers(
                        Routes.REST_ADMIN,
                        Routes.ScheduledTask.SCHEDULED_TASK_ANY).hasAuthority(ROLE_ADMIN.name())
                .anyRequest().authenticated();

        // Add JWT token filter before the form login filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // login
        //.and().formLogin().usernameParameter("email").loginProcessingUrl(LOGIN_PROCESSING_URL).defaultSuccessUrl("/", false).permitAll()
        //.and().logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
        // logout
        // .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll()

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().authenticationProvider(customUserJpaRepositoryAuthenticationProvider);
    }

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
