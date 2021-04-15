package by.it.academy.grodno.elibrary.entities.users;

import by.it.academy.grodno.elibrary.entities.AEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Table(name = ("address"))
public class Address extends AEntity<Long> implements Serializable {

    @Column(name = "address_1")
    private String region;

    @Column(name = "district")
    private String district;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "house")
    private String houseNumber;

    @Column(name = "apt")
    private String apartmentNumber;

    @Column(name = "last_updated")
    private LocalDateTime updated;
}
