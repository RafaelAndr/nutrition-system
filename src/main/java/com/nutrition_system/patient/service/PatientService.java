package com.nutrition_system.patient.service;

import com.nutrition_system.patient.entity.Patient;
import com.nutrition_system.patient.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final ClientRepository clientRepository;

    public void save(Patient patient){
        clientRepository.save(patient);
    }
}
