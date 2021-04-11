package by.it.academy.grodno.elibrary.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@Table(name = ("role"))
public class Role extends AEntity<Integer> implements GrantedAuthority, Serializable {

    @Column(name = "role_name")
    private String roleName;

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
