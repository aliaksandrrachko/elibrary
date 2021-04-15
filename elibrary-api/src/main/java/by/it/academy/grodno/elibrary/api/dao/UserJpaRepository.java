package by.it.academy.grodno.elibrary.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);
    Optional<User> findBySocialId(Long socialId);
}
