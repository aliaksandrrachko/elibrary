package by.it.academy.grodno.elibrary.api.dao.specifications;

import by.it.academy.grodno.elibrary.entities.users.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<User> userHasBirthday() {
        return (root, query, cb) -> cb.equal(root.get("birthday"), LocalDate.now());
    }

    public static Specification<User> isLongTermUser() {
        return (root, query, cb) -> cb.lessThan(root.get("created"), LocalDateTime.now().withNano(0).minusYears(2));
    }
}
