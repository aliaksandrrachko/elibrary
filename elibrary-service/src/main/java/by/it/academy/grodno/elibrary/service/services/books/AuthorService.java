package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.AuthorJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.AuthorDto;
import by.it.academy.grodno.elibrary.api.mappers.AuthorMapper;
import by.it.academy.grodno.elibrary.api.services.books.IAuthorService;
import by.it.academy.grodno.elibrary.entities.books.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService implements IAuthorService {

    private final AuthorJpaRepository authorJpaRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorJpaRepository authorJpaRepository, AuthorMapper authorMapper) {
        this.authorJpaRepository = authorJpaRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorDto> findAll() {
        return authorMapper.toDtos(authorJpaRepository.findAll());
    }

    @Override
    public AuthorDto findById(Integer id) {
        return authorMapper.toDto(authorJpaRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Optional<Author> authorOptional = authorJpaRepository.findById(id);
        authorOptional.ifPresent(authorJpaRepository::delete);
    }

    @Override
    @Transactional
    public Optional<AuthorDto> create(AuthorDto entityDto) {
        return Optional.of(authorMapper.toDto(authorJpaRepository.save(
                Author.builder()
                        .authorName(entityDto.getAuthorName())
                        .build())));
    }

    @Override
    @Transactional
    public Optional<AuthorDto> update(Integer id, AuthorDto entityDto) {
        Optional<Author> authorOptional = authorJpaRepository.findById(id);
        if (entityDto != null &&
                authorOptional.isPresent() &&
                entityDto.getAuthorName() != null &&
                !entityDto.getAuthorName().isEmpty()) {
            Author author = authorOptional.get();
            author.setAuthorName(entityDto.getAuthorName());
            author = authorJpaRepository.save(author);
            return Optional.of(authorMapper.toDto(author));
        }
        return authorOptional.map(authorMapper::toDto);
    }

    @Override
    public Page<AuthorDto> findAll(Pageable pageable) {
        return authorMapper.toPageDto(authorJpaRepository.findAll(pageable));
    }
}
