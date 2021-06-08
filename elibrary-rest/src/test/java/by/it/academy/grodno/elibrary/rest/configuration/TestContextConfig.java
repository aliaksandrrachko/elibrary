package by.it.academy.grodno.elibrary.rest.configuration;

import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContextConfig {

    @Bean
    public IPublisherService publisherService(){
        return Mockito.mock(IPublisherService.class);
    }
}
