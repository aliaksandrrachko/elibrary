package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.users.PhoneNumberDto;
import by.it.academy.grodno.elibrary.entitymetadata.users.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper  {

    public PhoneNumber toEntity(PhoneNumberDto source){
        return PhoneNumber.builder()
                .countryCode(source.getCountryCode())
                .nationalNumber(source.getNationalNumber())
                .build();
    }

    public PhoneNumberDto toDto(PhoneNumber source){
        if (source == null) {
            return new PhoneNumberDto();
        }
        return PhoneNumberDto.builder()
                .countryCode(source.getCountryCode())
                .nationalNumber(source.getNationalNumber())
                .build();
    }
}
