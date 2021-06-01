package by.it.academy.grodno.elibrary.service.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.mappers.AuthorMapper;
import by.it.academy.grodno.elibrary.entities.books.Author;
import by.it.academy.grodno.elibrary.service.services.books.AuthorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(value = {SpringExtension.class})
class AuthorServiceTest {

    @Mock
    private AuthorJpaRepository authorJpaRepository;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorService authorService;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(authorJpaRepository).isNotNull();
        assertThat(authorMapper).isNotNull();
        assertThat(authorService).isNotNull();
    }

    @Test
    void createAuthorTest() {
        final AuthorDto authorDto = getTestAuthorDto();
        when(authorJpaRepository.save(any(Author.class))).thenReturn(getTestAuthor());
        when(authorMapper.toDto(any(Author.class))).thenReturn(getTestAuthorDto());
        AuthorDto createdAuthorDto = authorService.create(authorDto);
        assertThat(createdAuthorDto).isNotNull();
        verify(authorJpaRepository, times(1)).save(any(Author.class));
    }

    @Test
    void findById() {
        final AuthorDto authorDto = getTestAuthorDto();
        final Author author = getTestAuthor();
        when(authorJpaRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(authorMapper.toDto(author)).thenReturn(authorDto);
        AuthorDto foundAuthor = authorService.findById(10);
        assertThat(foundAuthor).isNotNull();
        verify(authorJpaRepository, times(1)).findById(anyInt());
    }

    @Test
    void createAuthorDto() {
        final AuthorDto authorDto = new AuthorDto("Лев Николаевич Толстой");
        when(authorJpaRepository.save(any(Author.class))).thenAnswer((Answer<Author>) invocation -> {
            Author author = invocation.getArgument(0);
            author.setId(((int) (Math.random() * 100)));
            return author;
        });
        when(authorMapper.toDto(any(Author.class))).thenAnswer((Answer<AuthorDto>) invocation -> {
            Author answerAuthorDto = invocation.getArgument(0);
            return mapToDto(answerAuthorDto);
        });
        AuthorDto createdAuthorDto = authorService.create(authorDto);
        assertThat(createdAuthorDto).isNotNull();
        assertThat(createdAuthorDto.getId()).isNotNull();
        assertThat(createdAuthorDto.getAuthorName()).isEqualTo(authorDto.getAuthorName());
        verify(authorJpaRepository, times(1)).save(any(Author.class));
    }

    @Test
    void deleteAuthor() {
        final Author author = getTestAuthor();
        final Integer authorId = 10;
        when(authorJpaRepository.findById(authorId)).thenReturn(Optional.of(author));
        authorService.delete(authorId);
        verify(authorJpaRepository, times(1)).delete(any(Author.class));
        assertThat(authorJpaRepository.findById(authorId)).isNotNull();
    }

    @Test
    void updateAuthor() {
        final Author author = getTestAuthor();
        final Integer authorId = 10;
        when(authorJpaRepository.findById(authorId)).thenReturn(Optional.of(author));
        String authorNameToUpdate = "Georg Orwell";
        AuthorDto authorDtoForUpdate = new AuthorDto(authorNameToUpdate);
        authorService.update(authorId, authorDtoForUpdate);
        verify(authorJpaRepository, times(1)).save(any(Author.class));
        assertThat(authorJpaRepository.findById(authorId).orElse(new Author()).getAuthorName())
                .isEqualTo(authorNameToUpdate);
    }

    @Test
    void findAll() {
        final List<Author> authors = getTestAuthorList();
        when(authorJpaRepository.findAll()).thenReturn(authors);
        authorService.findAll();
        verify(authorMapper, times(1)).toDtos(authors);
    }

    private AuthorDto getTestAuthorDto() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(10);
        authorDto.setAuthorName("Erich Maria Remarque");
        return authorDto;
    }

    private Author getTestAuthor() {
        Author author = new Author();
        author.setId(10);
        author.setAuthorName("Erich Maria Remarque");
        return author;
    }

    private List<Author> getTestAuthorList() {
        List<Author> testAuthorList = new ArrayList<>();
        testAuthorList.add(new Author("Erich Maria Remarque"));
        testAuthorList.add(new Author("Georg Orwell"));
        testAuthorList.add(new Author("Лев Николаевич Толстой"));
        return testAuthorList;
    }

    private AuthorDto mapToDto(Author source) {
        AuthorDto authorDto = new AuthorDto(source.getAuthorName());
        authorDto.setId(source.getId());
        return authorDto;
    }
}

