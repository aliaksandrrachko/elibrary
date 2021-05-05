package by.it.academy.grodno.elibrary.api.dto.users;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AddressDto extends AEntityDto<Long> {

    @NotBlank(message = "Field 'Country' can not be empty")
    private String country;

    @NotBlank(message = "Field 'Region' can not be empty.")
    @Size(min = 4, message = "The 'Region' must be longer than 4 characters.")
    private String region;

    private String district;

    @NotBlank(message = "Field 'City' can not be empty.")
    private String cityName;

    @NotBlank(message = "Field 'Street' can not be empty.")
    private String streetName;

    private String postalCode;

    @NotBlank(message = "Field 'House number' can not be empty.")
    private String houseNumber;

    @NotBlank(message = "Field 'Apartment number' can not be empty.")
    private String apartmentNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;


    public boolean isFullAddress(){
        return StringUtils.hasText(country) &&
                StringUtils.hasText(region) &&
                StringUtils.hasText(cityName) &&
                StringUtils.hasText(streetName) &&
                StringUtils.hasText(postalCode) &&
                StringUtils.hasText(houseNumber) &&
                StringUtils.hasText(apartmentNumber);
    }
}
