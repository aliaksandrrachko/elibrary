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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.Optional;

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
    }

    private static final User test_user = new User("some@email.com",
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
            13516516541654L,
            null,
            null,
            null);

    @Rollback(false)
    @Test
    void saveUser() {
        User user = userJpaRepository.save(test_user);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress().getId()).isNotNull();
    }

    @Rollback(false)
    @Test
    void getUser(){
        Optional<User> userOptional = userJpaRepository.findByEmail(test_user.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(test_user.getFirstName());
    }

    @Rollback(false)
    @Test
    void updateUser(){
        test_user.setFirstName("updatedFirst");
        userJpaRepository.save(test_user);

        Optional<User> userOptional = userJpaRepository.findByEmail(test_user.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(test_user.getFirstName());
    }

    @Rollback(false)
    @Test
    void updateAddressUser(){
        test_user.getAddress().setApartmentNumber("100");
        userJpaRepository.save(test_user);

        Optional<User> userOptional = userJpaRepository.findByEmail(test_user.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getAddress().getApartmentNumber()).isEqualTo(test_user.getAddress().getApartmentNumber());
    }
}