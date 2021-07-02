package by.it.academy.grodno.elibrary.dao.hibernate;

import by.it.academy.grodno.elibrary.api.dao.hibernate.ICategoryDao;
import by.it.academy.grodno.elibrary.entities.books.Category;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

public class CategoryDao extends AGenericDao<Category, Integer> implements ICategoryDao {

    public CategoryDao() {
        super(Category.class);
    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        Query query = entityManager.createQuery("SELECT c FROM Category c WHERE c.categoryName LIKE :categoryName",
                Category.class);
        query.setParameter("categoryName", "%" + categoryName + "%");
        return Optional.ofNullable((Category) query.getSingleResult());
    }

    @Override
    public List<Category> findAllByParentCategoryIsNull() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> criteriaQuery = cb.createQuery(Category.class);
        Root<Category> categoryRoot = criteriaQuery.from(Category.class);
        criteriaQuery.select(categoryRoot).where(cb.equal(categoryRoot.get("parentCategory"), null));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
