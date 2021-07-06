package by.it.academy.grodno.elibrary.api.dao;

import by.it.academy.grodno.elibrary.api.dto.users.PublicUserDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmailOrSocialId(String email, Long socialId);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.id as userId, " +
            "u.email as email, " +
            "u.username as username, " +
            "u.first_name as firstName, " +
            "u.last_name as lastName, " +
            "u.middle_name as middleName, " +
            "u.avatar_url as avatarUrl, " +
            "usi.social_id as socialId "+
            "FROM public.user as u " +
            "LEFT OUTER JOIN user_social_id usi on u.id = usi.user_id" ,
        countQuery= "SELECT count(*) FROM public.user",
    nativeQuery = true)
    Page<PublicUserDetailsDto> findPublicUserDetailsDto(Pageable pageable);
}
