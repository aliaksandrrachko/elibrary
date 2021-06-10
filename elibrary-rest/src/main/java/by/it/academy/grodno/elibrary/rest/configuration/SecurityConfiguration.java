package by.it.academy.grodno.elibrary.rest.configuration;

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

    private static final String LOGIN_PAGE = "/login";
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
                .antMatchers("/rest/public/**").hasRole("USER")
                .antMatchers("/rest/admin/**").hasRole("ADMIN").anyRequest().authenticated()
                // login
                .and().formLogin().usernameParameter("email")
                .loginPage(LOGIN_PAGE).loginProcessingUrl(LOGIN_PROCESSING_URL).defaultSuccessUrl("/", true).permitAll()
                .and().logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
                // logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().authenticationProvider(customAuthenticationProvider)
        ;
    }
}
