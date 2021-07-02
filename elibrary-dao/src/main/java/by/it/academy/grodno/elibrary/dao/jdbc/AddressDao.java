package by.it.academy.grodno.elibrary.dao.jdbc;

import static by.it.academy.grodno.elibrary.dao.jdbc.MappersConstants.ADDRESS_ROW_MAPPER;

import by.it.academy.grodno.elibrary.api.dao.jdbc.IAddressDao;
import by.it.academy.grodno.elibrary.entities.users.Address;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class AddressDao extends AGenericDao<Address, Long> implements IAddressDao {

    private final JdbcTemplate jdbcTemplate;

    public AddressDao(JdbcTemplate jdbcTemplate) {
        super(Address.class);
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SELECT_ADDRESS =
            "SELECT id as address_id, country, region, district, city, " +
            "street, postal_code, house, apt, last_updated " +
            "FROM address ";

    private static final String SELECT_ADDRESS_WHERE_ID = SELECT_ADDRESS + "WHERE id = ?";

    @Override
    public List<Address> getAll() {
        return this.jdbcTemplate.query(
                SELECT_ADDRESS, ADDRESS_ROW_MAPPER);
    }

    @Override
    public Optional<Address> get(Long id) {

        Address address = this.jdbcTemplate.queryForObject(
                SELECT_ADDRESS_WHERE_ID, new Object[]{id}, ADDRESS_ROW_MAPPER);
        return Optional.ofNullable(address);
    }

    @Override
    public void update(Address entity) {
        this.jdbcTemplate.update(
                "UPDATE address SET " +
                        "country = ?, " +
                        "region = ?, " +
                        "district = ?, " +
                        "city = ?, " +
                        "street = ?, " +
                        "postal_code = ?, " +
                        "house = ?, " +
                        "apt = ? " +
                        "WHERE id = ?",
                entity.getCountry(),
                entity.getRegion(),
                entity.getDistrict(),
                entity.getCityName(),
                entity.getStreetName(),
                entity.getPostalCode(),
                entity.getHouseNumber(),
                entity.getApartmentNumber(),
                entity.getId());
    }

    private static final String INSERT_ADDRESS =
            "INSERT INTO address (country, region, district, city, street, postal_code, house, apt) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public Optional<Address> create(Address entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_ADDRESS, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getCountry());
            ps.setString(2, entity.getRegion());
            ps.setString(3, entity.getDistrict());
            ps.setString(4, entity.getCityName());
            ps.setString(5, entity.getStreetName());
            ps.setString(6, entity.getPostalCode());
            ps.setString(7, entity.getHouseNumber());
            ps.setString(8, entity.getApartmentNumber());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key != null){
            entity.setId(key.longValue());
        } else {
            throw new NoSuchElementException("Ket after insert equals:null.");
        }
        return Optional.of(entity);
    }

    @Override
    public void delete(Address entity) {
        this.jdbcTemplate.update("DELETE FROM address WHERE id = ?", entity.getId());
    }
}
