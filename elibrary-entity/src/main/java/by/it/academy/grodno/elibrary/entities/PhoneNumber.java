package by.it.academy.grodno.elibrary.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonPropertyOrder(value = {"code", "number"})
public class PhoneNumber implements Serializable {

    public static final String COUNTRY_CODE = "+375";

    @JsonProperty(value = "code")
    private String phoneCode;

    @JsonProperty(value = "number")
    private String number;
}
