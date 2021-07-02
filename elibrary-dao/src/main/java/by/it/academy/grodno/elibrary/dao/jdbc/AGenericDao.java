package by.it.academy.grodno.elibrary.dao.jdbc;

import by.it.academy.grodno.elibrary.api.dao.jdbc.IAGenericDao;
import by.it.academy.grodno.elibrary.entities.AEntity;

public abstract class AGenericDao<T extends AEntity<K>, K extends Number> implements IAGenericDao<T, K> {

    private final Class<T> aClass;

    protected AGenericDao(Class<T> aClass) {
        this.aClass = aClass;
    }

    @Override
    public Class<T> getGenericClass() {
        return aClass;
    }
}
