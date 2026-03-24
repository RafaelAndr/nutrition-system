package com.nutrition_system.patient.dto.resquest;

public record AddressRequestDto(
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
}
