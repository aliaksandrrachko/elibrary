package by.it.academy.grodno.elibrary.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This class represents common {@code interface} for common CRUD operations.
 * @param <T> type of entity
 * @param <K> key for Entities
 */
public interface IAGenericCrudService<T, K extends Number> {

    List<T> findAll();
    T findById(K id);
    void delete(K id);
    T create(T entityDto);
    T update(K id, T entityDto);
    Page<T> findAll(Pageable pageable);
}
