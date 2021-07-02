package by.it.academy.grodno.elibrary.dao.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import by.it.academy.grodno.elibrary.entities.users.Address;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.Rollback;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Optional;

class AddressJdbcDaoTest extends AJdbcTestDao {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private Environment env;

    @Autowired
    private AddressDao addressDao;

    @Test
    @Order(1)
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(addressDao).isNotNull();
        assertThat(env).isNotNull();
    }

    @Rollback(false)
    @Order(2)
    @Test
    void createAddress() {
        Address address = addressDao.create(getTestAddress()).orElse(null);
        assertThat(address).isNotNull();
        assertThat(address.getId()).isNotNull();
    }

    @Rollback(false)
    @Order(3)
    @Test
    void getAddress() {
        final Address testAddress = getTestAddress();
        Optional<Address> addressOptional = addressDao.get(testAddress.getId());
        Address addressFromDb = addressOptional.orElse(new Address());
        assertThat(addressFromDb.getApartmentNumber()).isEqualTo(testAddress.getApartmentNumber());
    }

    @Rollback(false)
    @Order(4)
    @Test
    void updateUser() {
        final Address testAddress = getTestAddress();
        testAddress.setApartmentNumber("1");
        addressDao.update(testAddress);

        Optional<Address> addressOptional = addressDao.get(testAddress.getId());
        Address addressFromDb = addressOptional.orElse(new Address());
        assertThat(addressFromDb.getApartmentNumber()).isEqualTo(testAddress.getApartmentNumber());
    }

    private Address getTestAddress() {
        return Address.builder()
                .id(1L)
                .country("Belarus")
                .region("Hrodna")
                .district("Hrodna")
                .cityName("Hrodna")
                .streetName("One")
                .postalCode("230005")
                .houseNumber("8418")
                .apartmentNumber("68")
                .updated(LocalDateTime.now())
                .build();
    }
}
