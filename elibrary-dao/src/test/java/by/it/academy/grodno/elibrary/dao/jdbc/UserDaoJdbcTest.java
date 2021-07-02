package by.it.academy.grodno.elibrary.dao.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.entitymetadata.users.Gender;
import by.it.academy.grodno.elibrary.entitymetadata.users.PhoneNumber;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.Optional;

class UserDaoJdbcTest extends AJdbcTestDao {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Autowired
    private UserDao userDao;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(userDao).isNotNull();
        assertThat(env).isNotNull();
    }

    @Rollback(false)
    @Order(1)
    @Test
    void saveUser() {
        final User userToCreating = getTestUser();
        User user = userDao.create(userToCreating).orElse(null);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getAddress().getId()).isNotNull();
    }

    @Rollback(false)
    @Order(2)
    @Test
    void getUser() {
        final User testUserData = getTestUser();
        Optional<User> userOptional = userDao.findByEmail(testUserData.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(testUserData.getFirstName());
    }

    @Rollback(false)
    @Order(3)
    @Test
    void updateUser() {
        final User testUser = getTestUser();
        testUser.setId(1L);
        testUser.setFirstName("updatedFirst");
        userDao.update(testUser);

        Optional<User> userOptional = userDao.findByEmail(testUser.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getFirstName()).isEqualTo(testUser.getFirstName());
    }

    @Rollback(false)
    @Order(4)
    @Test
    void updateAddressUser() {
        final User testUser = getTestUser();
        testUser.setId(1L);
        testUser.getAddress().setApartmentNumber("100");
        testUser.getAddress().setId(1L);
        userDao.update(testUser);

        Optional<User> userOptional = userDao.findByEmail(testUser.getEmail());
        User userFromDb = userOptional.orElse(new User());
        assertThat(userFromDb.getAddress().getApartmentNumber()).isEqualTo(testUser.getAddress().getApartmentNumber());
    }

    @Rollback(false)
    @Order(5)
    @Test
    void findUserEmailOrSocialId() {
        final User testUserdata = getTestUser();
        Optional<User> byEmailOrSocialId = userDao.findByEmailOrSocialId(testUserdata.getEmail(), testUserdata.getSocialId());
        User userFromDb = byEmailOrSocialId.orElse(new User());
        assertThat(userFromDb.getEmail()).isEqualTo(testUserdata.getEmail());
        assertThat(userFromDb.getSocialId()).isEqualTo(testUserdata.getSocialId());
    }

    private User getTestUser() {
        return new User("some@email.com",
                "Alex 1",
                "Alex",
                "Smirnov",
                "Petrovich",
                new PhoneNumber("375", "292205641"),
                new Address("Беларусь", "Гродненская", "Гродненский район", "Гродно",
                        "Терешковой", "230005", "50", "1a", null),
                Gender.MALE,
                LocalDate.of(2000, 12, 14),
                "$2a$10$Z1/.F4bRuyOGyL7NQrmjhufHf8XrHIEjPfBz9tlPbPcWrLpvPWKfq",
                true,
                "https://avatar-url.com/function/get_avatar",
                13516516541654L,
                null,
                null,
                null);
    }
}
