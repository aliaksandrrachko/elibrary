package by.it.academy.grodno.elibrary.rest.configuration;

import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import by.it.academy.grodno.elibrary.api.services.books.IReviewService;
import by.it.academy.grodno.elibrary.api.services.books.ISubscriptionScheduledTaskExecutorService;
import by.it.academy.grodno.elibrary.rest.utils.EntityJsonConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContextConfig {

    @Bean
    public IPublisherService publisherService(){
        return Mockito.mock(IPublisherService.class);
    }

    @Bean
    public ISubscriptionScheduledTaskExecutorService scheduledTaskExecutorService(){
        return Mockito.mock(ISubscriptionScheduledTaskExecutorService.class);
    }

    @Bean
    public IReviewService reviewService(){
        return Mockito.mock(IReviewService.class);
    }

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
        return mapper;
    }

    @Bean
    public EntityJsonConverter jsonConverter(){
        return new EntityJsonConverter();
    }
}
