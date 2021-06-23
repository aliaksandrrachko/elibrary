package by.it.academy.grodno.elibrary.main.configuration;

import by.it.academy.grodno.elibrary.main.server.EmbeddedTomcat;
import by.it.academy.grodno.elibrary.main.server.ServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(value = {"by.it.academy.grodno.elibrary"},
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {ServerConfig.class, EmbeddedTomcat.class}))
@EnableScheduling
@EnableAspectJAutoProxy
public class ApplicationRootConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties(){
        PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[ ]{ new ClassPathResource( "application.properties" ) };
        pspc.setLocations(resources);
        pspc.setIgnoreUnresolvablePlaceholders(true);
        return pspc;
    }
}
