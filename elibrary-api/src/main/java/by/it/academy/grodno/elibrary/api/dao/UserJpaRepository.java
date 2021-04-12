package by.it.academy.grodno.elibrary.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import by.it.academy.grodno.elibrary.entities.User;

public interface UserJpaRepository extends JpaRepository<User, Long>{

}
