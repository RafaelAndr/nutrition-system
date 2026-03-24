package com.nutrition_system.controller;

import com.nutrition_system.dto.response.PatientResponseDto;
import com.nutrition_system.dto.resquest.PatientRequestDto;
import com.nutrition_system.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@RequestBody PatientRequestDto patientRequestDto){

        PatientResponseDto patientResponseDto = patientService.createPatient(patientRequestDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientResponseDto.id()).toUri();

        return ResponseEntity.created(uri).body(patientResponseDto);
    }
}
