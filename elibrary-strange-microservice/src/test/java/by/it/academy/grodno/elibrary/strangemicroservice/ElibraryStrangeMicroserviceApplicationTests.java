package by.it.academy.grodno.elibrary.strangemicroservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ElibraryStrangeMicroserviceApplicationTests {

	@Autowired
	private Environment environment;

	@Test
	void contextLoads() {
		assertThat(environment).isNotNull();
	}
}
