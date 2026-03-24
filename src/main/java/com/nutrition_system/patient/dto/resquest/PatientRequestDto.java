package com.nutrition_system.patient.dto.resquest;

import com.nutrition_system.patient.entity.Address;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record PatientRequestDto(

        @NotBlank
        String name,

        @NotBlank
        @CPF
        String cpf,

        String telephone,

        @NotBlank
        LocalDate dateOfBirth,

        AddressRequestDto address
) {
}
