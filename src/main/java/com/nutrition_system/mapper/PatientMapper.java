package com.nutrition_system.mapper;

import com.nutrition_system.dto.response.PatientResponseDto;
import com.nutrition_system.dto.resquest.PatientRequestDto;
import com.nutrition_system.entity.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(PatientRequestDto patientRequestDto);

    PatientResponseDto toDto(Patient patient);
}
