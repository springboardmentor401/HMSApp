package com.hms.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import jakarta.validation.Validator;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.hms.repository.AppointmentRepository;
import com.hms.repository.PatientRepository;
import com.hms.entities.Patient;

import org.springframework.stereotype.Service;

import com.hms.entities.Patient;
import com.hms.repository.PatientRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EmailService emailService; // Autowired instance of EmailService

    public Patient addPatient(Patient patient) {
        patient.setStatus("Active");
        Patient newPatient = patientRepository.save(patient);

        // Notify patient via email
        sendEmailNotification(
                patient.getEmailId(), // Assuming you have 'emailId' in Patient entity
                "Patient Registration Successful",
                "Dear " + patient.getPatientName() + ",\n\n" +
                        "You have been successfully registered in our system.\n" +
                        "Thank you for choosing our services.\n\n" +
                        "Best Regards,\n"
                        + "Hospital Management Team"
        );

        return newPatient;
    }

    private void sendEmailNotification(String email, String subject, String text) {
        emailService.sendEmail(email, subject, text);  // Call the sendEmail method in EmailService
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

    public List<Patient> getPatientByName(String patientName) {
        return patientRepository.findByPatientName(patientName);
}
    public List<Patient> getPatientsByMedicalHistory(String medicalHistory) {
        return patientRepository.findByMedicalHistory(medicalHistory);
    }
    

    public Patient updatePatient(Patient patient) {
        Patient updatedPatient = patientRepository.save(patient);
        // Send email when a patient is updated
        emailService.notifyPatientUpdated(updatedPatient.getEmailId(), updatedPatient.getPatientName());
        return updatedPatient;
    }
    // Optional: Fetch patient by ID (if needed for PATCH requests)
    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Patient> getPatientsByDoctorAndDate(int doctorId, LocalDate appDate) {
        List<Patient> patients = appointmentRepository.findPatientsByDoctorAndDate(doctorId, appDate);
        if (patients == null || patients.isEmpty()) {
            // Optionally, log or throw a custom exception
            return List.of(); // Return empty list if no patients found
        }
        return patients;
    }

	public Patient updatePatientDetails(int id, Map<String, String> updates) {
		// TODO Auto-generated method stub
		return null;
	}
	
}


    
    








