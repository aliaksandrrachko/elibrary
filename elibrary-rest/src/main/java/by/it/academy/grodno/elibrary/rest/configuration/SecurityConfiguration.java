package by.it.academy.grodno.elibrary.rest.configuration;

import static by.it.academy.grodno.elibrary.api.Role.ROLE_ADMIN;
import static by.it.academy.grodno.elibrary.api.Role.ROLE_USER;
import static org.springframework.http.HttpMethod.*;

import by.it.academy.grodno.elibrary.api.constants.Routes;
import by.it.academy.grodno.elibrary.rest.utils.CustomAuthenticationProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@ComponentScan("by.it.academy.grodno.elibrary.rest.utils")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfiguration(CustomAuthenticationProvider customAuthenticationProvider, AccessDeniedHandler accessDeniedHandler) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/signup/**", "/signin/**", "/login/**").permitAll()
                // ANY_USER
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
                // FULL_USER
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
                // ADMIN
                .antMatchers(
                        Routes.REST_ADMIN,
                        Routes.ScheduledTask.SCHEDULED_TASK_ANY).hasAuthority(ROLE_ADMIN.name())
                .anyRequest().authenticated()
                // login
                .and().formLogin().usernameParameter("email").loginProcessingUrl(LOGIN_PROCESSING_URL).defaultSuccessUrl("/", false).permitAll()
                .and().logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
                // logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().authenticationProvider(customAuthenticationProvider)
        ;
    }
}
