package com.hms.service;

import com.hms.entities.Patient;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Add a new patient
    public Patient addPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    // Get all patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Get a patient by ID
    /*public Patient getPatientById(int id) {
        return patientRepository.findById(id).orElse(null);
    }*/
 
    public Patient getPatientById(int id) {
        return patientRepository.findById(id).orElse(null); // Returns null if patient is not found
    }

    // Get all patients (to solve the viewAllPatients issue)
    public List<Patient> viewAllPatients() {
        return patientRepository.findAll(); // Return a list of all patients
    }
}
