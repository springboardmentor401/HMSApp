package com.hms.controller;

import com.hms.entities.Patient;
import com.hms.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService service;

    // Add a new patient with validation and error handling
    @PostMapping("/addPatient")
    public ResponseEntity<?> addPatient(@Valid @RequestBody Patient patient, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Patient savedPatient = service.addPatient(patient);
            return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error while adding patient: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View all patients
    @GetMapping("/viewAllPatients")
    public ResponseEntity<List<Patient>> viewAllPatients() {
        try {
            List<Patient> patients = service.viewAllPatients();
            if (patients.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(patients);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // View a patient by ID with error handling
    @GetMapping("/viewPatientById/{id}")
    public ResponseEntity<?> viewPatientById(@PathVariable("id") int id) {
        try {
            Patient patient = service.getPatientById(id);
            if (patient == null) {
                return new ResponseEntity<>("Patient not found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(patient);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error while retrieving patient: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

   