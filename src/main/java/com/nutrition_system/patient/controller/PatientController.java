package com.nutrition_system.patient.controller;

import com.nutrition_system.patient.entity.Patient;
import com.nutrition_system.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public void save(@RequestBody Patient patient){
        patientService.save(patient);
    }
}
