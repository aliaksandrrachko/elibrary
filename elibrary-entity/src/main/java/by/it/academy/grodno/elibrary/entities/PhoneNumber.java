package by.it.academy.grodno.elibrary.entities;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PhoneNumber implements Serializable {

    public static final String COUNTRY_CODE = "+375";

    private String phoneCode;
    private String number;
}
