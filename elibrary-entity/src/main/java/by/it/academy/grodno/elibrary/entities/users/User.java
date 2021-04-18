package by.it.academy.grodno.elibrary.entities.users;

import by.it.academy.grodno.elibrary.entities.AEntity;
import by.it.academy.grodno.elibrary.entities.converters.GenderConverter;
import by.it.academy.grodno.elibrary.entities.converters.PhoneNumberJsonConverter;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = ("user"))
@SecondaryTables(value = {
        @SecondaryTable(name = "user_social_id", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "user_id")}),
        @SecondaryTable(name = "address", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})})
public class User extends AEntity<Long> implements UserDetails, Serializable {

    @Column(name = "email", unique = true, length = 80)
    private String email;

    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    //@Type(type = "json")
    @Convert(converter = PhoneNumberJsonConverter.class)
    @Column(name = "phone_number", columnDefinition = "json")
    private PhoneNumber phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "address_id")
    private Address address;

    /**
     * The gender of user.
     * m - male, f - female, u - unknown
     * <p>You can use it {@code private char gender;}
     */
    @Column(name = "gender")
    @Convert(converter = GenderConverter.class)
    @Builder.Default
    private Gender gender = Gender.UNKNOWN;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "password", unique = true, nullable = false, length = 64)
    private String password;

    @Column(name = "enabled")
    boolean enabled;

    @Column(name = "social_id", table = "user_social_id")
    private Long socialId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_has_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "user_created")
    private LocalDateTime created;

    @Column(name = "user_updated")
    private LocalDateTime updated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}