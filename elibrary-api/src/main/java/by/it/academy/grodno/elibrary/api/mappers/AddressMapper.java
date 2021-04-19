package by.it.academy.grodno.elibrary.api.mappers;

import by.it.academy.grodno.elibrary.api.dto.AddressDto;
import by.it.academy.grodno.elibrary.entities.users.Address;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper extends AGenericMapper<Address, AddressDto, Long>{

    protected AddressMapper(ModelMapper modelMapper) {
        super(modelMapper, Address.class, AddressDto.class);
    }
}
