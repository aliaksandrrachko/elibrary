package by.it.academy.grodno.elibrary.api.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserJpaRepositoryTest {

    //@Autowired
    //private UserJpaRepository userJpaRepository;

    private static final String TEST_USER_NAME = "Admin";

    @Test
    void findByUsername() {

    }
}