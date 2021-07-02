package by.it.academy.grodno.elibrary.api.dao.jdbc;

import by.it.academy.grodno.elibrary.entities.users.User;

import java.util.Optional;

public interface IUserDao extends IAGenericDao<User, Long>{

    void deleteRole(Long id, Integer roleId);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrSocialId(String email, Long socialId);
}
