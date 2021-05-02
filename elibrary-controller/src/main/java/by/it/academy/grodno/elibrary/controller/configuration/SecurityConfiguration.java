package by.it.academy.grodno.elibrary.controller.configuration;

import by.it.academy.grodno.elibrary.controller.utils.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final AccessDeniedHandler accessDeniedHandler;

    public SecurityConfiguration(CustomAuthenticationProvider customAuthenticationProvider,
                                 AccessDeniedHandler accessDeniedHandler) {
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired
    @Qualifier("customOAuth2UserService")
    OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/signup/**", "/signin/**", "/login/**", "/js/**", "/css/**"
                        , "/images/**", "/oauth/**").permitAll()
                .antMatchers("/subscriptions/**").hasRole("USER")
                .antMatchers("/rest/**").permitAll() // for testing REST Controller
                .antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()
                // login
                .and().formLogin().usernameParameter("email")
                .loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/", true).permitAll()
                .and().logout().deleteCookies("JSESSIONID").invalidateHttpSession(true).clearAuthentication(true)
                // logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().authenticationProvider(customAuthenticationProvider)
                // oAuth2
                .oauth2Login().loginPage("/login").defaultSuccessUrl("/", true).permitAll()
                .userInfoEndpoint().userService(customOAuth2UserService)
        ;
    }
}

