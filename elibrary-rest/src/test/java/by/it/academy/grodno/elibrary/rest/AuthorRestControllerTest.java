package by.it.academy.grodno.elibrary.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.WebServicesAutoConfiguration;
import org.springframework.boot.autoconfigure.webservices.client.WebServiceTemplateAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.webservices.client.WebServiceClientTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// test using real server
@ExtendWith(value = {SpringExtension.class})
@MockBeans(value = {@MockBean(IAuthorService.class)})
//@AutoConfigureWebMvc
@EnableWebMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = {
                TomcatServletWebServerFactory.class,

               // AllEncompassingFormHttpMessageConverter.class,
               DispatcherServletAutoConfiguration.class,
               WebMvcAutoConfiguration.class,
//                WebServicesAutoConfiguration.class,
//                WebServiceTemplateAutoConfiguration.class,
//                WebServiceClientTemplateAutoConfiguration.class,
//                JacksonAutoConfiguration.class,
////                SpringDataJacksonConfiguration.class,
////                SpringDataWebAutoConfiguration.class,
////                ValidationAutoConfiguration.class,
                HttpMessageConvertersAutoConfiguration.class,
////                RestDocsAutoConfiguration.class,
////                HttpEncodingAutoConfiguration.class,
////                ErrorMvcAutoConfiguration.class,
////                EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
////                ServletWebServerFactoryAutoConfiguration.class,
////                MultipartAutoConfiguration.class,
//
                AuthorRestController.class
        })
class AuthorRestControllerTest {

    private static final String LOCAL_HOST = "http://localhost:";

    @LocalServerPort
    private int port;
    @Mock
    private IAuthorService authorService;
    @InjectMocks
    private AuthorRestController authorRestController;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(authorRestController).isNotNull();
        assertThat(authorService).isNotNull();
        assertThat(restTemplate).isNotNull();
    }

    @Test
    void findAuthor() {
        final AuthorDto authorDto = getTestAuthorDto();
        final int authorId = 10;
        when(authorService.findById(any())).thenReturn(authorDto);
        ResponseEntity<AuthorDto> responseEntity = this.restTemplate
                .getForEntity(LOCAL_HOST + port + "/rest/authors/" + authorId, AuthorDto.class);
        AuthorDto entity = responseEntity.getBody();
        assertThat(entity).isNotNull().isEqualTo(authorDto);
    }

    @Test
    void findAllAuthors(){
        final Page<AuthorDto> pageOfAuthor = getTestAuthorDtoPage();
        when(authorService.findAll(any(Pageable.class))).thenReturn(pageOfAuthor);
        String pageAuthorDotResponse = this.restTemplate
                .getForObject(LOCAL_HOST + port + "/rest/authors", String.class);
        assertThat(pageAuthorDotResponse).isNotNull().isEqualTo(pageOfAuthor);
    }

    @Test
    void deleteAuthor(){
        final int authorId = 1124;
        this.restTemplate.delete(LOCAL_HOST + port + "/rest/authors/" + authorId);
        verify(authorService, times(1)).delete(authorId);
    }

    @Test
    void updateAuthor(){
        final AuthorDto authorDtoForUpdate = getTestAuthorDto();
        final int authorId = 10;
        String response = this.restTemplate.getForObject(LOCAL_HOST + port + "/rest/authors/" + authorId, String.class);
        verify(authorService, times(1)).update(authorId, authorDtoForUpdate);
    }

    private static final AuthorDto TEST_AUTHOR_DTO;

    static {
        TEST_AUTHOR_DTO = new AuthorDto("Erich Maria Remarque");
        TEST_AUTHOR_DTO.setId(10);
    }

    private AuthorDto getTestAuthorDto() {
        return TEST_AUTHOR_DTO;
    }


    private static final List<AuthorDto> TEST_AUTHOR_LIST;

    static {
        TEST_AUTHOR_LIST = new ArrayList<>();
        TEST_AUTHOR_LIST.add(new AuthorDto("Erich Maria Remarque"));
        TEST_AUTHOR_LIST.add(new AuthorDto("Georg Orwell"));
        TEST_AUTHOR_LIST.add(new AuthorDto("Лев Николаевич Толстой"));
    }

    private Page<AuthorDto> getTestAuthorDtoPage(){
        return new PageImpl<>(TEST_AUTHOR_LIST);
    }
}
