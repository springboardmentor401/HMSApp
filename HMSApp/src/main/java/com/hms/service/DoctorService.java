package com.hms.service;

import com.hms.entities.Doctor;
import com.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    // Add a doctor
    public Doctor addDoctor(Doctor doctor) {
    	System.out.println("Saving doctor to the database: " + doctor);// Debugging the doctor object before saving
        Doctor saveDoctor = doctorRepository.save(doctor);
        System.out.println("Doctor saved successfully with ID: " + saveDoctor.getDoctorId()); // Confirm save
        return saveDoctor;
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        System.out.println("Fetching all doctors from the database...");
        List<Doctor> doctors = doctorRepository.findAll();
        System.out.println("Total doctors retrieved: " + doctors.size()); // Print number of doctors
        return doctors;
    }

    // Get a doctor by ID
    public Doctor getDoctorById(Long id) {
        System.out.println("Fetching doctor with ID: " + id);
        return doctorRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("Doctor with ID " + id + " not found."); // Error case log
                    return new RuntimeException("Doctor with ID " + id + " not found.");
                });
    }

    // Update a doctor
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        System.out.println("Updating doctor with ID: " + id + " using data: " + updatedDoctor);
        Optional<Doctor> optionalDoctor = doctorRepository.findById(id);

        if (optionalDoctor.isPresent()) {
            Doctor existingDoctor = optionalDoctor.get();
            System.out.println("Existing doctor found: " + existingDoctor);

            // Update only the fields that are provided
            existingDoctor.setDoctorName(updatedDoctor.getDoctorName());
            existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
            existingDoctor.setQualification(updatedDoctor.getQualification());
            existingDoctor.setContactNumber(updatedDoctor.getContactNumber());
            existingDoctor.setEmailId(updatedDoctor.getEmailId());
            existingDoctor.setGender(updatedDoctor.getGender());
            existingDoctor.setLocation(updatedDoctor.getLocation());
            existingDoctor.setConsultationFees(updatedDoctor.getConsultationFees());
            existingDoctor.setDateOfJoining(updatedDoctor.getDateOfJoining());
            existingDoctor.setSurgeon(updatedDoctor.isSurgeon());
            existingDoctor.setYearsOfExperience(updatedDoctor.getYearsOfExperience());
            existingDoctor.setStatus(updatedDoctor.isStatus());

            Doctor savedDoctor = doctorRepository.save(existingDoctor);
            System.out.println("Doctor updated successfully: " + savedDoctor);
            return savedDoctor;
        } else {
            System.err.println("Doctor with ID " + id + " not found for update."); // Error case log
            throw new RuntimeException("Doctor with ID " + id + " not found.");
        }
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        try {
            return doctorRepository.findBySpecialization(specialization);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching doctors by specialization: " + e.getMessage());
        }
    }
}