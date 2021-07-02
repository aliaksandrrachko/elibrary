package by.it.academy.grodno.elibrary.api.dao.jdbc;

import by.it.academy.grodno.elibrary.entities.AEntity;

import java.util.List;
import java.util.Optional;

public interface IAGenericDao<T extends AEntity<K>, K extends Number> {

    Class<T> getGenericClass();
    List<T> getAll();
    Optional<T> get(K id);
    Optional<T> create(T entity);
    void update(T entity);
    void delete(T entity);
}
