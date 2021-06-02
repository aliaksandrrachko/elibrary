package by.it.academy.grodno.elibrary.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application.properties")
class PropertyInjectionUnitTest {

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Test
    void whenFilePropertyProvided_thenProperlyInjected() {
        assertThat(driverClassName).isEqualTo("org.h2.Driver");
    }
}
