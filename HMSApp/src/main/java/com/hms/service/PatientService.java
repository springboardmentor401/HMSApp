
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
        try {
            patient.setStatus("Active");

            return patientRepository.save(patient);  // Save the patient in the database
        } catch (Exception ex) {
            throw new RuntimeException("Error while adding patient: " + ex.getMessage());
        }
    }

    // View all patients
    public List<Patient> viewAllPatients() {
        try {
            return patientRepository.findAll();  // Retrieve all patients from the database
        } catch (Exception ex) {
            throw new RuntimeException("Error while retrieving patients: " + ex.getMessage());
        }
    }

    // Get a patient by ID
    public Patient getPatientById(int id) {
        try {
            Optional<Patient> patient = patientRepository.findById(id);  // Find patient by ID
            return patient.orElse(null);  // Return patient if found, otherwise null
        } catch (Exception ex) {
            throw new RuntimeException("Error while retrieving patient by ID: " + ex.getMessage());
        }
    }
}
