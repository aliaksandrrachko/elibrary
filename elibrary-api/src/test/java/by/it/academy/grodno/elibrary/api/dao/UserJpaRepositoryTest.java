package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.users.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EntityScan(basePackages = "by.it.academy.grodno.elibrary.entities")
@ComponentScan(basePackages = "by.it.academy.grodno.elibrary.dao")
@EnableJpaRepositories
// Its for ignore intellij IDEA warnings
@ContextConfiguration(classes = {UserJpaRepository.class})
class UserJpaRepositoryTest {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(userJpaRepository).isNotNull();
        String stop = "stop";
    }

    private static final User TEST_USER = new User("some@email.com",
            "Alex 1",
            "Alex",
            "Smirnov",
            "Petrovich",
            new PhoneNumber("29", "2205641"),
            new Address("Гродненская", "Гродненский район", "Гродно",
                    "Терешковой", "230005", "50", "1a", null),
            Gender.MALE,
            LocalDate.of(2000, 12, 14),
            "$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq",
            true,
            null,
            Collections.singleton(new Role("ROLE_USER")),
            null,
            null);

    /*
    @Test
    void saveUser() {
        assertThat(userJpaRepository.save(TEST_USER)).isNotNull();
    }

    @Test
    void getUser(){
        Optional<User> userOptional = userJpaRepository.findByEmail(TEST_USER.getEmail());
        assertThat(userOptional.orElse(new User()).getFirstName()).isEqualTo(TEST_USER.getFirstName());
    }*/
}