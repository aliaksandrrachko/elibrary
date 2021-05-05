package by.it.academy.grodno.elibrary.api.dto.users;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PhoneNumberDto implements Serializable {

    @NotBlank(message = "Field 'Country code' can not be empty.")
    @Pattern(regexp = "^\\d{1,4}$", message = "Only digits.")
    private String countryCode;

    @NotBlank(message = "Field 'Number' can not be empty.")
    @Pattern(regexp = "^\\d{9}$", message = "Only nine digits.")
    private String nationalNumber;
}
