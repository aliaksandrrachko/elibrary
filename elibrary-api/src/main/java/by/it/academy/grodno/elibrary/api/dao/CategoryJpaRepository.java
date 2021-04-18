package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Integer> {

    Optional<Category> findByCategoryName(String categoryName);
}
