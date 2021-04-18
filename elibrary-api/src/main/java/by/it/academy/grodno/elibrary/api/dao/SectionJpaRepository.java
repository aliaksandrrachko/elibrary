package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionJpaRepository extends JpaRepository<Section, Integer> {

    Optional<Section> findBySectionName(String sectionName);
}
