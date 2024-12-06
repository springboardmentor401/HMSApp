package com.hms.controller;

import com.hms.entities.Doctor;
import com.hms.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // add a new doctor
    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor newDoctor = doctorService.addDoctor(doctor);
        doctorService.sendEmailNotification("New doctor added: " + doctor.getDoctorName());
        return ResponseEntity.ok(newDoctor);
    }

    // get all doctors
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    // get a doctor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable int id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    // deactivate a doctor
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Doctor> deactivateDoctor(@PathVariable int id) {
        Doctor updatedDoctor = doctorService.deactivateDoctor(id);
        doctorService.sendEmailNotification("Doctor deactivated: " + updatedDoctor.getDoctorName());
        return ResponseEntity.ok(updatedDoctor);
    }
}
