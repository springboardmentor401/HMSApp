package com.hms.controller;

import com.hms.entities.Doctor;

import com.hms.exception.InvalidEntityException;
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
    public ResponseEntity<?> addDoctor(@Valid @RequestBody Doctor doctor) {
        
        
            doctorService.addDoctor(doctor);
            return new ResponseEntity<>("Doctor added successfully!", HttpStatus.CREATED);
    }
    

    // Get all doctors
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors() {
       
            List<Doctor> doctors = doctorService.getAllDoctors();
            return new ResponseEntity<>(doctors, HttpStatus.OK);
       
    }

    // Get a doctor by ID
    @GetMapping("/getDoctor/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable int id) throws InvalidEntityException {
      
            Doctor doctor = doctorService.getDoctorById(id);
            if (doctor == null) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(doctor, HttpStatus.OK);
        }
   

    @PutMapping("/updateDoctor/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable int id, @RequestBody Doctor doctor) throws InvalidEntityException {
       
            Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
            if (updatedDoctor == null) {
                return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Doctor updated successfully!", HttpStatus.OK);
        } 
    
        @PutMapping("/leaveDoctor/{id}")
        public ResponseEntity<String> deactivateDoctor(@PathVariable int id) throws InvalidEntityException{
            
                boolean isDeactivated = doctorService.deactivateDoctor(id);
                if (isDeactivated) {
                    return ResponseEntity.ok("Doctor successfully deactivated.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
                }
           
            }
 
 
 // Get doctors by specialization
    @GetMapping("/getBySpecialization/{specialization}")
    public ResponseEntity<?> getDoctorsBySpecialization(@PathVariable String specialization) throws InvalidEntityException {
       
            List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
            if (doctors.isEmpty()) {
                return new ResponseEntity<>("No doctors found for this specialization", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } 

}
