package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.PublisherJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.mappers.PublisherMapper;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PublisherService implements IPublisherService {

    private final PublisherMapper publisherMapper;
    private final PublisherJpaRepository publisherJpaRepository;

    public PublisherService(PublisherMapper publisherMapper, PublisherJpaRepository publisherJpaRepository) {
        this.publisherMapper = publisherMapper;
        this.publisherJpaRepository = publisherJpaRepository;
    }

    @Override
    public List<PublisherDto> findAll() {
        return publisherMapper.toDtos(publisherJpaRepository.findAll());
    }

    @Override
    public PublisherDto findById(Integer id) {
        return publisherMapper.toDto(publisherJpaRepository.findById(id).orElse(null));
    }

    @Override
    public void delete(Integer id) {
        Optional<Publisher> publisherOptional = publisherJpaRepository.findById(id);
        publisherOptional.ifPresent(publisherJpaRepository::delete);
    }

    @Override
    @Transactional
    public Optional<PublisherDto> create(PublisherDto entityDto) {
        return Optional.of(publisherMapper.toDto(publisherJpaRepository.save(
                Publisher.builder()
                        .publisherName(entityDto.getPublisherName())
                        .build())));
    }

    @Override
    @Transactional
    public Optional<PublisherDto> update(Integer id, PublisherDto entityDto) {
        Optional<Publisher> optionalPublisher = publisherJpaRepository.findById(id);
        if (entityDto != null &&
                optionalPublisher.isPresent() &&
                entityDto.getPublisherName() != null &&
                !entityDto.getPublisherName().isEmpty()) {
            Publisher publisher = optionalPublisher.get();
            publisher.setPublisherName(entityDto.getPublisherName());
            publisher = publisherJpaRepository.save(publisher);
            return Optional.of(publisherMapper.toDto(publisher));
        }
        return optionalPublisher.map(publisherMapper::toDto);
    }

    @Override
    public Page<PublisherDto> findAll(Pageable pageable) {
        return publisherMapper.toPageDto(publisherJpaRepository.findAll(pageable));
    }
}
