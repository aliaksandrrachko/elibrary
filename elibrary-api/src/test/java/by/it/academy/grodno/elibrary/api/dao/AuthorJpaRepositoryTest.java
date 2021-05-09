package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Author;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@EntityScan(basePackages = "by.it.academy.grodno.elibrary.entities")
@ComponentScan(basePackages = "by.it.academy.grodno.elibrary.dao")
@EnableJpaRepositories
// Its for ignore intellij IDEA warnings
@ContextConfiguration(classes = {AuthorJpaRepository.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorJpaRepositoryTest {

    private static final Author TEST_AUTHOR = new Author("Erich Maria Remarque");
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private AuthorJpaRepository authorJpaRepository;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(authorJpaRepository).isNotNull();
    }

    @Rollback(false)
    @Test
    @Order(1)
    void saveAuthor() {
        Author author = authorJpaRepository.save(TEST_AUTHOR);
        TEST_AUTHOR.setId(author.getId());
        assertThat(author.getId()).isNotNull();
    }

    @Rollback(false)
    @Test
    @Order(2)
    void getAuthor() {
        List<Author> authorList = authorJpaRepository.findByAuthorNameContaining(TEST_AUTHOR.getAuthorName());
        assertThat(authorList)
                .isNotEmpty()
                .contains(TEST_AUTHOR);
    }

    @Rollback(false)
    @Test
    @Order(3)
    void updateAuthor() {
        TEST_AUTHOR.setAuthorName("Georg Orwell");
        Author author = authorJpaRepository.save(TEST_AUTHOR);
        assertThat(author).isEqualTo(TEST_AUTHOR);
    }

    @Rollback(false)
    @Test
    @Order(4)
    void deleteAuthor() {
        authorJpaRepository.delete(TEST_AUTHOR);
        Optional<Author> authorOptional = authorJpaRepository.findById(TEST_AUTHOR.getId());
        assertThat(authorOptional).isNotPresent();
    }
}
