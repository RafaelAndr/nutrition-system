package com.nutrition_system.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDto(
        UUID id,
        String name,
        String cpf,
        String city,
        LocalDate registrationDate
) {
}
