package by.it.academy.grodno.elibrary.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @NotBlank(message = "Field 'Region' can not be empty.")
    @Size(min = 4, message = "The 'Region' must be longer than 4 characters.")
    private String region;

    private String district;

    @NotBlank(message = "Field 'City' can not be empty.")
    private String cityName;


    private String postalCode;

    @NotBlank(message = "Field 'House number' can not be empty.")
    private String houseNumber;

    @NotBlank(message = "Field 'Apartment number' can not be empty.")
    private String apartmentNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime updated;
}
