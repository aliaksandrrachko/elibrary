package by.it.academy.grodno.elibrary.dao.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.entities.books.Book;
import by.it.academy.grodno.elibrary.entities.books.Category;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

class BookDaoTest extends AHibernateTestDao {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private CategoryDao categoryDao;

    @Test
    @Order(1)
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(bookDao).isNotNull();
        assertThat(categoryDao).isNotNull();
    }

    @Rollback(false)
    @Test
    @Order(2)
    void saveCategory() {
        final Category testCategory = getTestCategory();
        Category category = categoryDao.create(testCategory);
        assertThat(category).isNotNull();
        assertThat(category.getId()).isNotNull();
    }

    @Rollback(false)
    @Test
    @Order(3)
    void saveBook() {
        final Book testBook = getTestBook();
        final Category testCategory = getTestCategory();
        Category categoryFromDb = categoryDao.findByCategoryName(testCategory.getCategoryName()).orElse(null);
        testBook.setCategory(categoryFromDb);
        Book book = bookDao.create(testBook);
        assertThat(book).isNotNull();
        assertThat(book.getId()).isNotNull();
    }

    @Rollback(false)
    @Test
    @Order(4)
    void findBookByTitleLike() {
        Page<Book> allByTitleLike = bookDao.findAllByTitleLike(getTestBook().getTitle(), PageRequest.of(0, 10));
        assertThat(allByTitleLike).isNotNull();
        assertThat(allByTitleLike.getContent().size()).isEqualTo(1);
        assertThat(allByTitleLike.getTotalElements()).isEqualTo(1);
    }

    private Book getTestBook() {
        return new Book("5171350728", "9785040988358", "Рэй Брэдбери: Вино из одуванчиков",
                "Яркое, фантастическое лето 1928 года: двенадцатилетний Дуглас Сполдинг ведет записи о событиях того лета, которые складываются в отдельные истории, гротескные искажения ординарных будней маленького городка, где живут Дуглас и его семья. Там все кажется не тем, чем является, а сила детского воображения создает новую реальность, которую не отличить от вымысла. Выросший из отдельных рассказов, филигранных в своей лиричности, роман \"Вино из одуванчиков\" — классическая хроника детства Рэя Брэдбери, окно в творческий мир писателя, создавшего такие шедевры мировой литературы, как \"Марсианские хроники\" и \"451 градус по Фаренгейту\".',\n",
                new Category("Художественная литература", null, null),
                new Publisher("TEXT"),
                Collections.singleton(new Author("George Orwell")),
                null,
                "rus",
                LocalDate.of(2019, 1, 1),
                256,
                "https://img3.labirint.ru/rc/3ed50890722042ff78db15eb83c84530/220x340/books73/720915/cover.jpg?1617258303",
                2, 2, true, 3, LocalDateTime.now().withNano(0), LocalDateTime.now().withNano(0));
    }

    private Category getTestCategory() {
        return new Category("Художественная литература", null, null);
    }
}
