package by.it.academy.grodno.elibrary.dao.jdbc;

import by.it.academy.grodno.elibrary.dao.jdbc.utils.GenericJsonConverter;
import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.Role;
import by.it.academy.grodno.elibrary.entities.users.User;
import by.it.academy.grodno.elibrary.entitymetadata.users.Gender;
import by.it.academy.grodno.elibrary.entitymetadata.users.PhoneNumber;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappersConstants {

    private MappersConstants() {
    }

    static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) ->
            Role.builder()
                    .id(rs.getInt("role_id"))
                    .roleName("role_name")
                    .build();

    static final RowMapper<Address> ADDRESS_ROW_MAPPER = (rs, rowNum) -> Address.builder()
            .id(rs.getLong("address_id"))
            .country(rs.getString("country"))
            .region(rs.getString("region"))
            .district(rs.getString("district"))
            .cityName(rs.getString("city"))
            .streetName(rs.getString("street"))
            .postalCode(rs.getString("postal_code"))
            .houseNumber(rs.getString("house"))
            .apartmentNumber(rs.getString("apt"))
            .updated(rs.getTimestamp("last_updated") == null ? null : rs.getTimestamp("last_updated").toLocalDateTime())
            .build();

    static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> User.builder()
            .id(rs.getLong("user_id"))
            .email(rs.getString("email"))
            .username(rs.getString("username"))
            .firstName(rs.getString("first_name"))
            .lastName(rs.getString("last_name"))
            .middleName(rs.getString("middle_name"))
            .phoneNumber(GenericJsonConverter.convertToEntity(rs.getString("phone_number"), PhoneNumber.class))
            .address(ADDRESS_ROW_MAPPER.mapRow(rs, rowNum))
            .roles(Collections.singleton(ROLE_ROW_MAPPER.mapRow(rs, rowNum)))
            .gender(Gender.getGender(rs.getString("gender").charAt(0)))
            .birthday(rs.getTimestamp("birthday") == null ? null : rs.getTimestamp("birthday").toLocalDateTime().toLocalDate())
            .password(rs.getString("password"))
            .enabled(rs.getBoolean("enabled"))
            .avatarUrl(rs.getString("avatar_url"))
            .socialId(rs.getLong("social_id"))
            .created(rs.getTimestamp("user_created") == null ? null : rs.getTimestamp("user_created").toLocalDateTime())
            .updated(rs.getTimestamp("user_updated") == null ? null : rs.getTimestamp("user_updated").toLocalDateTime())
            .build();

    static final ResultSetExtractor<Map<Long, User>> MAP_USER_ID_USER_RESULT_SET_EXTRACTOR = rs -> {
        Map<Long, User> result = new HashMap<>();
        while (rs.next()) {
            User user = USER_ROW_MAPPER.mapRow(rs, rs.getRow());
            User userFromMap = result.get(user.getId());
            if (userFromMap == null) {
                result.put(user.getId(), user);
            } else {
                userFromMap.getRoles().addAll(user.getRoles());
            }
        }
        return result;
    };
}
