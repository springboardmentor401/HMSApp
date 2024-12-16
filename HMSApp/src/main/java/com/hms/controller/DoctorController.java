package com.hms.controller;

import com.hms.entities.Doctor;
import com.hms.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Add a new doctor
    @PostMapping("/addDoctor")
    public ResponseEntity<?> addDoctor(@Valid @RequestBody Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            doctorService.addDoctor(doctor);
            return new ResponseEntity<>("Doctor added successfully!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while adding doctor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all doctors
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while fetching doctors: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a doctor by ID
    @GetMapping("/getDoctor/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);
            if (doctor == null) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while fetching doctor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a doctor
    @PutMapping("/updateDoctor/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @Valid @RequestBody Doctor doctor, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
            if (updatedDoctor == null) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Doctor updated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating doctor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a doctor
 // Update the status when a doctor leaves (instead of deleting the doctor)
    @PutMapping("/leaveDoctor/{id}")
    public ResponseEntity<?> leaveDoctor(@PathVariable Long id) {
        try {
            // Fetch the doctor by ID
            Doctor doctor = doctorService.getDoctorById(id);
            if (doctor == null) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }

            // Update the status to false (indicating the doctor has left)
            doctor.setStatus(false);
            doctorService.updateDoctor(id, doctor);

            return new ResponseEntity<>("Doctor's status updated to 'left'.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating doctor status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
 // Get doctors by specialization
    @GetMapping("/getBySpecialization/{specialization}")
    public ResponseEntity<?> getDoctorsBySpecialization(@PathVariable String specialization) {
        try {
            List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
            if (doctors.isEmpty()) {
                return new ResponseEntity<>("No doctors found for this specialization", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while fetching doctors: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
