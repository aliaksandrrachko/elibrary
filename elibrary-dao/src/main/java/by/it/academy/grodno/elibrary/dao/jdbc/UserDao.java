package by.it.academy.grodno.elibrary.dao.jdbc;

import static by.it.academy.grodno.elibrary.dao.jdbc.MappersConstants.MAP_USER_ID_USER_RESULT_SET_EXTRACTOR;
import static by.it.academy.grodno.elibrary.dao.jdbc.MappersConstants.USER_ROW_MAPPER;

import by.it.academy.grodno.elibrary.api.dao.jdbc.IAddressDao;
import by.it.academy.grodno.elibrary.api.dao.jdbc.IUserDao;
import by.it.academy.grodno.elibrary.dao.jdbc.utils.GenericJsonConverter;
import by.it.academy.grodno.elibrary.entities.users.Address;
import by.it.academy.grodno.elibrary.entities.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class UserDao extends AGenericDao<User, Long> implements IUserDao {

    private final JdbcTemplate jdbcTemplate;

    private final IAddressDao addressDao;

    public UserDao(JdbcTemplate jdbcTemplate, IAddressDao addressDao) {
        super(User.class);
        this.jdbcTemplate = jdbcTemplate;
        this.addressDao = addressDao;
    }

    private static final String SQL_SELECT_USER = "SELECT " +
            "u.id as user_id, email, username, first_name, last_name, middle_name, phone_number, gender, birthday, " +
            "password, enabled, avatar_url, user_created, user_updated, " +
            "social_id, " +
            "a.id as address_id, country, region, district, city, street, postal_code, house, apt, last_updated, " +
            "r.id as role_id, role_name " +
            "FROM public.user as u " +
            "LEFT OUTER JOIN address a on u.address_id = a.id " +
            "LEFT OUTER JOIN user_social_id usi on u.id = usi.user_id "+
            "LEFT OUTER JOIN user_has_role uhr on u.id = uhr.user_id " +
            "LEFT OUTER JOIN role r on uhr.role_id = r.id ";

    private static final String SQL_SELECT_USER_ORDER_BY_ID = SQL_SELECT_USER + "ORDER BY user_id";

    private static final String SQL_SELECT_USER_WHERE_ID = SQL_SELECT_USER + "WHERE u.id = ?";

    private static final String SQL_SELECT_USER_WHERE_EMAIL = SQL_SELECT_USER + "WHERE u.email = ? ";

    @Override
    public List<User> getAll() {
        return Objects.requireNonNull(jdbcTemplate.query(SQL_SELECT_USER_ORDER_BY_ID, MAP_USER_ID_USER_RESULT_SET_EXTRACTOR))
                .values().parallelStream().collect(Collectors.toList());
    }

    @Override
    public Optional<User> get(Long id) {
        User userFromDb = this.jdbcTemplate.queryForObject(SQL_SELECT_USER_WHERE_ID, new Object[]{id}, USER_ROW_MAPPER);
        return Optional.ofNullable(userFromDb);
    }

    private static final String SQL_INSERT_USER =
            "INSERT INTO public.user (" +
                    "email, username, first_name, last_name, middle_name," +
                    " phone_number, address_id, gender, birthday, password, enabled, avatar_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public Optional<User> create(User entity) {
        Address createdAddress = addressDao.create(entity.getAddress()).orElse(null);
        entity.setAddress(createdAddress);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getUsername());
            ps.setString(3, entity.getFirstName());
            ps.setString(4, entity.getLastName());
            ps.setString(5, entity.getMiddleName());
            ps.setString(6, GenericJsonConverter.convertToDatabaseColumn(entity.getPhoneNumber()));
            ps.setLong(7, entity.getAddress().getId());
            ps.setString(8, String.valueOf(entity.getGender().getAbbreviation()));
            ps.setTimestamp(9, Timestamp.valueOf(entity.getBirthday().atStartOfDay()));
            ps.setString(10, entity.getPassword());
            ps.setBoolean(11, entity.isEnabled());
            ps.setString(12, entity.getAvatarUrl());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null){
            entity.setId(key.longValue());
            insertIntoUserHasRole(entity);
            insertIntoUserSocialIdIfNeeded(entity);
        } else {
            throw new NoSuchElementException("Key after insert equals:null.");
        }
        return Optional.of(entity);
    }

    private static final String SQL_INSERT_USER_HAS_ROLE = "INSERT INTO user_has_role (user_id, role_id) VALUES (?, ?)";

    private void insertIntoUserHasRole(User entity){
        if (entity.getRoles() == null || entity.getRoles().isEmpty()){
            return;
        }
        List<Object[]> list = entity.getRoles().stream()
                .map(role -> new Object[]{entity.getId(), role.getId()})
                .collect(Collectors.toList());
        this.jdbcTemplate.batchUpdate(SQL_INSERT_USER_HAS_ROLE, list);
    }

    private static final String SQL_INSERT_USER_SOCIAL_ID = "INSERT INTO user_social_id (user_id, social_id) VALUES (?, ?)";

    private void insertIntoUserSocialIdIfNeeded(User entity){
        if (entity.getSocialId() == null) {
            return;
        }
        this.jdbcTemplate.update(SQL_INSERT_USER_SOCIAL_ID, entity.getId(), entity.getSocialId());
    }

    private static final String SQL_UPDATE_USER = "UPDATE public.user SET " +
            "email = ?, " +
            "username = ?, " +
            "first_name = ?, " +
            "last_name = ?, " +
            "middle_name = ?, " +
            "phone_number = ?, " +
            "address_id = ?, " +
            "gender = ?, " +
            "birthday = ?, " +
            "password = ?, " +
            "enabled = ?, " +
            "avatar_url = ? " +
            "WHERE id = ?";

    @Override
    public void update(User entity) {
        this.jdbcTemplate.update(SQL_UPDATE_USER,
                entity.getEmail(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getMiddleName(),
                GenericJsonConverter.convertToDatabaseColumn(entity.getPhoneNumber()),
                entity.getAddress().getId(),
                entity.getGender().getAbbreviation(),
                Timestamp.valueOf(entity.getBirthday().atStartOfDay()),
                entity.getPassword(),
                entity.isEnabled(),
                entity.getAvatarUrl(),
                entity.getId());
        addressDao.update(entity.getAddress());
        updateOrDeleteAddressIfExist(entity);
        updateOrDeleteUserSocialId(entity);
        updateOrDeleteIntoUserHasRole(entity);
    }

    private static final String SQL_SELECT_ROLES_WHERE_USER_ID =
                    "INSERT INTO user_has_role (user_id, role_id) VALUES (?, ?) ON CONFLICT (user_id, role_id) DO NOTHING";

    private void updateOrDeleteIntoUserHasRole(User entity) {
        if (entity.getRoles() == null || entity.getRoles().isEmpty()){
            return;
        }
        this.jdbcTemplate.batchUpdate(SQL_SELECT_ROLES_WHERE_USER_ID,
                entity.getRoles().stream()
                        .map(role -> new Object[]{entity.getId(), role.getId()})
                        .collect(Collectors.toList()));
    }

    private void updateOrDeleteUserSocialId(User entity){
        if (entity.getSocialId() == null) {
            this.jdbcTemplate.update("DELETE FROM user_social_id WHERE user_id = ?");
        } else {
            this.jdbcTemplate.update(
                    "UPDATE user_social_id SET social_id = ? WHERE user_id = ?", entity.getSocialId(), entity.getId());
            this.jdbcTemplate.update(
                    "INSERT INTO user_social_id (user_id, social_id) " +
                            "SELECT ?, ? WHERE NOT EXISTS(SELECT 1 FROM user_social_id WHERE user_id = ?)",
                    entity.getId(), entity.getSocialId(), entity.getId());
        }
    }

    private void updateOrDeleteAddressIfExist(User entity){
        if (entity.getAddress() == null){
            jdbcTemplate.update(
                    "DELETE FROM address WHERE id = (SELECT u.address_id FROM public.user u WHERE u.id = ?)"
                    , entity.getId());
        } else {
            addressDao.update(entity.getAddress());
        }
    }

    @Override
    public void delete(User entity) {
        this.jdbcTemplate.update("DELETE FROM public.user WHERE id = ?", entity.getId());
    }

    @Override
    public void deleteRole(Long id, Integer roleId) {
        this.jdbcTemplate.update("DELETE FROM user_has_role WHERE user_id = ? AND role_id = ?", id, roleId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Map<Long, User> query = this.jdbcTemplate.query(SQL_SELECT_USER_WHERE_EMAIL,
                new Object[]{email},
                MAP_USER_ID_USER_RESULT_SET_EXTRACTOR);
        return getOptionalUserFromMap(query);
    }

    private Optional<User> getOptionalUserFromMap(Map<Long, User> query) {
        if (query != null && !query.isEmpty()) {
            return query.values().stream().findFirst();
        } else {
            return Optional.empty();
        }
    }

    private static final String SLQ_SELECT_USER_WHERE_EMAIL_OR_SOCIAL_ID = SQL_SELECT_USER +
            "WHERE email = ? OR social_id = ?";

    @Override
    public Optional<User> findByEmailOrSocialId(String email, Long socialId) {
        Map<Long, User> query = this.jdbcTemplate.query(SLQ_SELECT_USER_WHERE_EMAIL_OR_SOCIAL_ID, new Object[]{email, socialId}, MAP_USER_ID_USER_RESULT_SET_EXTRACTOR);
        return getOptionalUserFromMap(query);
    }
}
