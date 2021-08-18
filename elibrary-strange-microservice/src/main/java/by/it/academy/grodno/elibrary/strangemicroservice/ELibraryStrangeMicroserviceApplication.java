package by.it.academy.grodno.elibrary.strangemicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients
@EnableReactiveFeignClients(basePackages = {"by.it.academy.grodno.elibrary"})
public class ELibraryStrangeMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ELibraryStrangeMicroserviceApplication.class, args);
	}
}
