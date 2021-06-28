package by.it.academy.grodno.elibrary.rest.controllers;

import by.it.academy.grodno.elibrary.rest.configuration.TestContextConfig;
import by.it.academy.grodno.elibrary.rest.configuration.WebMvcConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ComponentScan("by.it.academy.grodno.elibrary.rest.controllers")
@EnableWebSecurity
@ContextConfiguration(
        classes = {PublisherRestController.class,
                ScheduledTaskSubscriptionController.class,
                ReviewRestController.class,
                WebMvcConfig.class, TestContextConfig.class})
@WebAppConfiguration
class ARestControllerTest {
}
