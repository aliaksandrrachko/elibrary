package by.it.academy.grodno.elibrary.api.dao;

import static org.assertj.core.api.Assertions.assertThat;

import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.entitymetadata.users.Gender;
import by.it.academy.grodno.elibrary.entitymetadata.users.PhoneNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Optional;

class UserJpaRepositoryTest extends ATestJpaRepositories {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(userJpaRepository).isNotNull();
    }

    private static final User TEST_USER = new User("some@email.com",
            "Alex 1",
            "Alex",
            "Smirnov",
            "Petrovich",
           null,
            new Address("Беларусь", "Гродненская", "Гродненский район", "Гродно",
                    "Терешковой", "230005", "50", "1a", null),
            Gender.MALE,
            LocalDate.of(2000, 12, 14),
            "$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq",
            true,
            null,
            13516516541654L,
            null,
            null,
            null);

    @Rollback(false)
    @Test
    void saveUser() {
        User user = userJpaRepository.save(TEST_USER);
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress().getId()).isNotNull();
    }

    @Rollback(false)
    @Test
    void getUser(){
        Optional<User> userOptional = userJpaRepository.findByEmail(TEST_USER.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(TEST_USER.getFirstName());
    }

    @Rollback(false)
    @Test
    void updateUser(){
        TEST_USER.setFirstName("updatedFirst");
        userJpaRepository.save(TEST_USER);

        Optional<User> userOptional = userJpaRepository.findByEmail(TEST_USER.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(TEST_USER.getFirstName());
    }

    @Rollback(false)
    @Test
    void updateAddressUser(){
        TEST_USER.getAddress().setApartmentNumber("100");
        userJpaRepository.save(TEST_USER);

        Optional<User> userOptional = userJpaRepository.findByEmail(TEST_USER.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getAddress().getApartmentNumber()).isEqualTo(TEST_USER.getAddress().getApartmentNumber());
    }
}