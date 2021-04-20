package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.entities.users.PhoneNumber;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PhoneNumberMapper {

    public static String toStringNumber(PhoneNumber phoneNumber){
        StringBuilder sb = new StringBuilder("");
        if (phoneNumber != null) {
            sb.append(phoneNumber.getPhoneCode());
            sb.append(phoneNumber.getNumber());
        }
        return sb.toString();
    }

    private static final String PHONE_NUMBER_PATTERN = "^\\d{9}$";

    public static PhoneNumber toEntity(String string){
        if (string != null && string.matches(PHONE_NUMBER_PATTERN)) {
            return PhoneNumber.builder()
                    .phoneCode(string.substring(0, 2))
                    .number(string.substring(2))
                    .build();
        } else {
            return null;
        }
    }
}
