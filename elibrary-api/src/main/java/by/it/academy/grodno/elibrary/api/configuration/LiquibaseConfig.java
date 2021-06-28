package by.it.academy.grodno.elibrary.api.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
@ComponentScan("by.it.academy.grodno.elibrary.api.configuration")
public class LiquibaseConfig {

    @Value("${spring.liquibase.change-log}")
    private String changeLog;
    @Value("${spring.liquibase.enabled}")
    private boolean enable;
    @Value("${spring.liquibase.default-schema}")
    private String defaultSchema;
    @Value("${spring.liquibase.drop-first}")
    private boolean dropFirst;

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource, ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource(changeLog);
        Assert.state(resource.exists(), "Unable to find file: " + changeLog);

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changeLog);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setDropFirst(dropFirst);
        liquibase.setShouldRun(enable);
        return liquibase;
    }
}
