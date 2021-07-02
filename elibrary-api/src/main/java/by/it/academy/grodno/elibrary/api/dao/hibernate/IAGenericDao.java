package by.it.academy.grodno.elibrary.api.dao.hibernate;

import by.it.academy.grodno.elibrary.entities.AEntity;

import java.util.List;
import java.util.Optional;

public interface  IAGenericDao<T extends AEntity<K>, K extends Number> {

    Class<T> getGenericClass();
    List<T> getAll();
    Optional<T> get(K id);
    void delete(T entity);
    T create(T entity);
    T update(T entity);
}
