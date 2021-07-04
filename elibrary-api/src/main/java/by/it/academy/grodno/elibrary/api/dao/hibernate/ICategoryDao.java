package by.it.academy.grodno.elibrary.api.dao.hibernate;

import by.it.academy.grodno.elibrary.entities.books.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryDao extends IAGenericDao<Category, Integer> {

    Optional<Category> findByCategoryName(String categoryName);
    List<Category> findAllByParentCategoryIsNull();
    Optional<Category> findOnlyCategory(Integer id);
}
