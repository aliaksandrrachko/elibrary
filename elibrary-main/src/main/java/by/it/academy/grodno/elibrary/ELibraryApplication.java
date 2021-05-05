package by.it.academy.grodno.elibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class ELibraryApplication {
        public static void main(String[] args) {
            SpringApplication.run(ELibraryApplication.class, args);
        }
}
