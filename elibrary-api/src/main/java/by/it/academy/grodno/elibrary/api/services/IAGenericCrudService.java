package by.it.academy.grodno.elibrary.api.services;

import java.util.List;
import java.util.Optional;

/**
 * This class represents common {@code interface} for all services.
 * @param <T> type of entity
 * @param <K> key for Entities
 */
public interface IAGenericCrudService<T, K extends Number> {
    
    Class<T> getGenericClass();

    List<T> findAll();
    Optional<T> findById(K id);
    void delete(K id);
    T save(T entityDto);
    T update(K id, T entityDto);
}
