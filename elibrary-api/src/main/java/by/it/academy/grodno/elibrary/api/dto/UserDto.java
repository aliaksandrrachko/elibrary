package by.it.academy.grodno.elibrary.api.dto;

import by.it.academy.grodno.elibrary.entities.users.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends AEntityDto<Long> {

    boolean enabled;
    @NotBlank(message = "Field 'Email' can not be empty.")
    @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)" +
            "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)" +
            "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)" +
            "+(?:[a-zA-Z]){2,}\\.?)$",
            message = "Invalid 'Email'")
    private String email;
    @NotBlank(message = "Field 'Username' can not be empty.")
    @Size(min = 2, max = 20, message = "The 'Username' must be between 2 and 20 characters.")
    private String username;
    @NotBlank(message = "Field 'First name' can not be empty.")
    @Size(min = 2, max = 20, message = "The 'First name' must be between 2 and 20 characters.")
    private String firstName;
    @NotBlank(message = "Field 'Last name' can not be empty.")
    @Size(min = 2, max = 20, message = "The 'First name' must be between 2 and 20 characters.")
    private String lastName;
    @NotBlank(message = "Field 'Middle name' can not be empty.")
    @Size(min = 2, max = 20, message = "The 'Middle name' must be between 2 and 20 characters.")
    private String middleName;
    @NotBlank(message = "Field 'Phone' can not be empty.")
    @Pattern(regexp = "^\\d{9}$", message = "'Phone' can be in format (xx)xxxxxxx")
    private String phoneNumber;
    private AddressDto addressDto;
    private Character gender;
    private LocalDate birthday;
    @NotBlank(message = "Field 'Password' can not be empty.")
    @Size(min = 4, message = "The 'Password' must be longer than 4 characters.")
    private String password;
    @NotBlank(message = "Field 'Password confirm' can not be empty.")
    @Size(min = 4, message = "The 'Password' must be longer than 4 characters.")
    private String passwordConfirm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime updated;
    private Long socialId;
    private Set<String> roles;

    public Set<String> getRoles(){
        if (roles == null){
            return new HashSet<>();
        } else {
            return roles;
        }
    }
}