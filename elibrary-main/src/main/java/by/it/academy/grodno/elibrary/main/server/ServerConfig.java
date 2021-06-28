package by.it.academy.grodno.elibrary.main.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"by.it.academy.grodno.elibrary.main.server"})
@PropertySource("classpath:server.properties")
public class ServerConfig {

}