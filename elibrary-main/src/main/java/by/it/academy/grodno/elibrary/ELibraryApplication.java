package by.it.academy.grodno.elibrary;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@PropertySource(value = "classpath:/application.properties")
public class ELibraryApplication {
        public static void main(String[] args) {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext("by.it.academy.grodno.elibrary");

            for (String beanDefinitionName : context.getBeanDefinitionNames()) {
                System.out.println(beanDefinitionName);
            }

            Object jpaRepository = context.getBean("authorJpaRepository");
            if (jpaRepository != null){
                AuthorJpaRepository authorJpaRepository = (AuthorJpaRepository) jpaRepository;
                authorJpaRepository.findById(1);
            }
        }
}
