package by.it.academy.grodno.elibrary.api.dto.users;

import by.it.academy.grodno.elibrary.api.dto.AEntityDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @Builder.Default
    boolean enabled = true;
    @NotBlank(message = "Field 'Email' can not be empty.")
    @Pattern(regexp = "^(?:[a-zA-Z0-9_'^&/+-])+(?:\\.(?:[a-zA-Z0-9_'^&/+-])+)" +
            "*@(?:(?:\\[?(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))\\.)" +
            "{3}(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\]?)|(?:[a-zA-Z0-9-]+\\.)" +
            "+(?:[a-zA-Z]){2,}\\.?)$",
            message = "Invalid 'Email'")
    private String email;
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
    @Valid
    private AddressDto addressDto = new AddressDto();
    private String gender;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Field 'Birthday' can not be empty.")
    private LocalDate birthday;
    @NotBlank(message = "Field 'Password' can not be empty.")
    @Size(min = 4, message = "The 'Password' must be longer than 4 characters.")
    private String password;
    @NotBlank(message = "Field 'Password confirm' can not be empty.")
    @Size(min = 4, message = "The 'Password' must be longer than 4 characters.")
    private String passwordConfirm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;
    private Long socialId;
    private Set<String> roles;

    public Set<String> getRoles(){
        if (roles == null){
            return roles = new HashSet<>();
        } else {
            return roles;
        }
    }
}