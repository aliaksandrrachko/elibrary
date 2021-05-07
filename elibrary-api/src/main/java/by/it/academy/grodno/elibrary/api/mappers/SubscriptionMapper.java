package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dao.BookJpaRepository;
import by.it.academy.grodno.elibrary.api.dao.UserJpaRepository;
import by.it.academy.grodno.elibrary.api.dto.books.SubscriptionDto;
import by.it.academy.grodno.elibrary.entities.books.Subscription;
import by.it.academy.grodno.elibrary.entitymetadata.books.SubscriptionStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;

@Component
public class SubscriptionMapper extends AGenericMapper<Subscription, SubscriptionDto, Long>{

    private final BookMapper bookMapper;

    private final BookJpaRepository bookJpaRepository;

    private final UserJpaRepository userJpaRepository;

    protected SubscriptionMapper(ModelMapper modelMapper, BookMapper bookMapper, BookJpaRepository bookJpaRepository,
                                 UserJpaRepository userJpaRepository) {
        super(modelMapper, Subscription.class, SubscriptionDto.class);
        this.bookMapper = bookMapper;
        this.bookJpaRepository = bookJpaRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Subscription.class, SubscriptionDto.class)
                .addMappings(s -> {
                    s.skip(SubscriptionDto::setStatus);
                    s.skip(SubscriptionDto::setBookId);
                    s.skip(SubscriptionDto::setBookDetails);
                    s.skip(SubscriptionDto::setUserId);
                    s.skip(SubscriptionDto::setUsername);
                    s.skip(SubscriptionDto::setUserEmail);
                }).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(SubscriptionDto.class, Subscription.class)
                .addMappings(s -> {
                    s.skip(Subscription::setStatus);
                    s.skip(Subscription::setBook);
                    s.skip(Subscription::setUser);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(Subscription source, SubscriptionDto destination) {
        destination.setStatus(source.getStatus().name().toLowerCase());
        destination.setBookId(source.getBook().getId());
        destination.setBookDetails(bookMapper.toDto(source.getBook()));
        destination.setUserId(source.getUser().getId());
        destination.setUsername(source.getUser().getUsername());
        destination.setUserEmail(source.getUser().getEmail());
    }

    @Override
    void mapSpecificFields(SubscriptionDto source, Subscription destination) {
        destination.setStatus(SubscriptionStatus.valueOf(source.getStatus().toLowerCase()));
        destination.setBook(bookJpaRepository.findById(source.getBookId()).orElseThrow(NoSuchElementException::new));
        destination.setUser(userJpaRepository.findById(source.getUserId()).orElseThrow(NoSuchElementException::new));
    }
}
