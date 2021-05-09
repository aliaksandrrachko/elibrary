package by.it.academy.grodno.elibrary.service.services;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.mappers.AuthorMapper;
import by.it.academy.grodno.elibrary.api.mappers.ModelMapperConfig;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.service.services.books.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(value = {MockitoExtension.class})
@SpringBootTest
@EntityScan(basePackages = {"by.it.academy.grodno.elibrary.entities",
                            "by.it.academy.grodno.elibrary.api.dto"})
@ContextConfiguration(classes = {AuthorService.class, AuthorMapper.class, ModelMapperConfig.class})
class AuthorServiceTest {

    @MockBean
    private AuthorJpaRepository authorJpaRepository;

    @InjectMocks
    private AuthorService authorService;

    @Autowired
    private AuthorMapper authorMapper;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(authorJpaRepository).isNotNull();
        assertThat(authorService).isNotNull();
        assertThat(authorMapper).isNotNull();
    }

    @Test
    void whenFindAuthorByIdThenReturnAuthorDto() {
        Optional<Author> author = Optional.of(new Author("Erich Maria Remarque"));
        doReturn(author).when(authorJpaRepository).findById(1);
        Optional<AuthorDto> optionalAuthor = authorService.findById(1);
        assertThat(optionalAuthor).isPresent();
        assertThat(optionalAuthor.get().getAuthorName()).isEqualTo(author.get().getAuthorName());
    }
}

