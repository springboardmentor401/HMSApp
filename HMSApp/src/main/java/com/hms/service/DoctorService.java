package com.hms.service;

import com.hms.entities.Doctor;
import com.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Add a new doctor
    public Doctor addDoctor(Doctor doctor) {
        doctor.setStatus(true); // Default to active status when adding a new doctor
        return doctorRepository.save(doctor);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // Get doctor by ID
    public Doctor getDoctorById(int doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
    }

    // Deactivate a doctor
    public Doctor deactivateDoctor(int doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setStatus(false); // status of inactive
        return doctorRepository.save(doctor);
    }

    // Additional email notification logic can be implemented here
    public void sendEmailNotification(String message) {
        
        System.out.println("Email sent: " + message);
    }
}
