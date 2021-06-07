package by.it.academy.grodno.elibrary.api;

import static org.assertj.core.api.Assertions.assertThat;

import by.it.academy.grodno.elibrary.api.configuration.TestJpaRepositoriesConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes = {TestJpaRepositoriesConfig.class},
        loader = AnnotationConfigContextLoader.class)
class PropertyInjectionUnitTest{

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Test
    void whenFilePropertyProvided_thenProperlyInjected() {
        assertThat(driverClassName).isEqualTo("org.h2.Driver");
    }
}
