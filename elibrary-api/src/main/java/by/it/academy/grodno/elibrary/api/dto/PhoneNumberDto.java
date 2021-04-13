package by.it.academy.grodno.elibrary.api.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberDto {

    public static final String COUNTRY_CODE = "+375";

    @NotBlank(message = "Phone code can not be empty.")
    @Size(min = 2, max = 2, message = "Phone code must be contains 2 digits.")
    private String phoneCode;

    @NotBlank(message = "Phone number can not be empty.")
    @Size(min = 7, max = 7, message = "Phone number must be contains 7 digits.")
    private String number;
}
