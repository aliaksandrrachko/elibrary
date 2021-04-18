package by.it.academy.grodno.elibrary.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonPropertyOrder(value = {"code", "number"})
public class PhoneNumber implements Serializable {

    @JsonIgnoreProperties
    public static final String COUNTRY_CODE = "+375";

    @JsonProperty(value = "code")
    private String phoneCode;

    @JsonProperty(value = "number")
    private String number;
}
