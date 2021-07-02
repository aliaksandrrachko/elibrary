package by.it.academy.grodno.elibrary.dao.hibernate;

import by.it.academy.grodno.elibrary.api.configuration.PersistenceJpaConfig;
import by.it.academy.grodno.elibrary.dao.hibernate.configuration.TestHibernateDaoConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@ComponentScan("by.it.academy.grodno.elibrary.dao.hibernate")
@ContextConfiguration(
        classes = {TestHibernateDaoConfig.class, PersistenceJpaConfig.class},
        loader = AnnotationConfigContextLoader.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class AHibernateTestDao {


}
