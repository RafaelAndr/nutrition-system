package com.nutrition_system.mapper;

import com.nutrition_system.dto.resquest.AddressRequestDto;
import com.nutrition_system.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressRequestDto addressRequestDto);

}
