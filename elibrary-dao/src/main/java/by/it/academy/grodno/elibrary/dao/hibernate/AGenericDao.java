package by.it.academy.grodno.elibrary.dao.hibernate;

import by.it.academy.grodno.elibrary.api.dao.hibernate.IAGenericDao;
import by.it.academy.grodno.elibrary.entities.AEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class AGenericDao<T extends AEntity<K>, K  extends Number>
        implements IAGenericDao<T,  K> {

    private final Class<T> clazz;

    protected AGenericDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public Class<T> getGenericClass() {
        return this.clazz;
    }

    @Override
    public List<T> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    @Override
    public Optional<T> get(K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public void delete(T entity) {
        if (entityManager.contains(entity)) {
            entityManager.remove(entity);
        } else {
            entityManager.remove(entityManager.merge(entity));
        }
    }

    @Override
    public T create(T entity) {
        this.entityManager.persist(entity);
        this.entityManager.flush();
        return entity;
    }

    @Override
    public T update(T entity) {
        return this.entityManager.merge(entity);
    }
}
