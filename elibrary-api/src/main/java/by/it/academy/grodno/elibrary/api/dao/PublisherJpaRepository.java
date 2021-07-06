package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.books.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublisherJpaRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByPublisherName(String publisherName);

    @EntityGraph(value = "publisher-books-entity-graph")
    Page<Publisher> findPublisherWithBookByPublisherNameLike(String publisherName, Pageable pageable);
}
