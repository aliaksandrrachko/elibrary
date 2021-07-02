package by.it.academy.grodno.elibrary.dao.hibernate.configuration;

import by.it.academy.grodno.elibrary.dao.hibernate.BookDao;
import by.it.academy.grodno.elibrary.dao.hibernate.CategoryDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application.properties")
public class TestHibernateDaoConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]{ new ClassPathResource( "application.properties" ) };
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        return pspc;
    }

    @Bean
    public BookDao bookDao(){
        return new BookDao();
    }

    @Bean
    public CategoryDao categoryDao(){
        return new CategoryDao();
    }
}
