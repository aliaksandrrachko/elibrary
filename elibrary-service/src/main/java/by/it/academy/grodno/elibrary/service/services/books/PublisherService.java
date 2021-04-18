package by.it.academy.grodno.elibrary.service.services.books;

import by.it.academy.grodno.elibrary.api.dao.PublisherJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.PublisherDto;
import by.it.academy.grodno.elibrary.api.mappers.PublisherMapper;
import by.it.academy.grodno.elibrary.api.services.books.IPublisherService;
import by.it.academy.grodno.elibrary.entities.books.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PublisherService implements IPublisherService {

    @Autowired
    private PublisherMapper publisherMapper;

    @Autowired
    private PublisherJpaRepository publisherJpaRepository;

    @Override
    public Class<PublisherDto> getGenericClass() {
        return PublisherDto.class;
    }

    @Override
    public List<PublisherDto> findAll() {
        return publisherMapper.toDtos(publisherJpaRepository.findAll());
    }

    @Override
    public Optional<PublisherDto> findById(Integer id) {
        Optional<Publisher> optionalCategory = publisherJpaRepository.findById(id);
        return optionalCategory.map(publisher -> publisherMapper.toDto(publisher));

    }

    @Override
    public void delete(Integer id) {
        Optional<Publisher> publisherOptional = publisherJpaRepository.findById(id);
        publisherOptional.ifPresent(publisher -> publisherJpaRepository.delete(publisher));
    }

    @Override
    public Optional<PublisherDto> create(PublisherDto entityDto) {
        return Optional.of(publisherMapper.toDto(publisherJpaRepository.save(
                Publisher.builder()
                        .publisherName(entityDto.getPublisherName())
                        .build())));
    }

    @Override
    public Optional<PublisherDto> update(Integer id, PublisherDto entityDto) {
        Optional<Publisher> optionalPublisher = publisherJpaRepository.findById(id);
        if (entityDto != null &&
        optionalPublisher.isPresent() &&
        entityDto.getPublisherName() != null &&
        !entityDto.getPublisherName().isEmpty()){
            Publisher publisher = optionalPublisher.get();
            publisher.setPublisherName(entityDto.getPublisherName());
            publisher = publisherJpaRepository.save(publisher);
            return Optional.of(publisherMapper.toDto(publisher));
        }
        return optionalPublisher.map(publisher -> publisherMapper.toDto(publisher));
    }

    @Override
    public Page<PublisherDto> findAll(Pageable pageable) {
        return publisherMapper.toPageDto(publisherJpaRepository.findAll(pageable));
    }
}
