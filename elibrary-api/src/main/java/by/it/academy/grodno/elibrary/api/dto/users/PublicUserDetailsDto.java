package by.it.academy.grodno.elibrary.api.dto.users;

import org.springframework.beans.factory.annotation.Value;

public interface PublicUserDetailsDto {

    Long getUserId();

    String getEmail();

    String getUsername();

    @Value("#{target.firstName + ' ' + target.lastName + ' ' + target.middleName}")
    String getFullName();

    String getAvatarUrl();

    Long getSocialId();
}
