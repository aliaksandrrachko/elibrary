package by.it.academy.grodno.elibrary.entities.users;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "address")
public class Address extends AEntity<Long> implements Serializable {

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "region", length = 50)
    private String region;

    @Column(name = "district", length = 50)
    private String district;

    @Column(name = "city", length = 50)
    private String cityName;

    @Column(name = "street", length = 50)
    private String streetName;

    @Column(name = "postal_code", length = 32)
    private String postalCode;

    @Column(name = "house", length = 10)
    private String houseNumber;

    @Column(name = "apt", length = 10)
    private String apartmentNumber;

    @Column(name = "last_updated", insertable = false, updatable = false)
    private LocalDateTime updated;
}
