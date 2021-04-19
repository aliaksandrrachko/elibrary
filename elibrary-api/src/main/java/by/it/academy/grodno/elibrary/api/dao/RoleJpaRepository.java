package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.entities.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(String roleName);
}
